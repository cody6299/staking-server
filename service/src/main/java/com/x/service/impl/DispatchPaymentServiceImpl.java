package com.x.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.*;
import org.web3j.abi.TypeEncoder;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.web3j.abi.datatypes.*;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Hash;
import java.math.*;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.dao.po.staking.*;
import com.x.common.utils.*;
import com.x.service.*;

@Slf4j
@Service
public class DispatchPaymentServiceImpl implements DispatchPaymentService {

    @Resource
    ChainService chainService;
    @Resource
    ChainPaymentService chainPaymentService;
    @Resource
    UserDailyRewardService userDailyRewardService;
    @Resource
    BindRelationService bindRelationService;
    @Resource
    DispatchRewardService dispatchRewardService;
    @Resource
    MerkleTreeService merkleTreeService;
    @Resource
    PaymentInfoService paymentInfoService;
    @Resource
    VaultContractService vaultContractService;
    String vaultCaller;
    @Value("${caller.vault:0x7286d8b6a41A3cE6e2360bb62eBc5B15059c2166}")
    public void setVaultCaller(String address) {
        this.vaultCaller = ContractUtil.addressDeserialize(address);
    }
    String vaultContract;
    @Value("${contract.vault:0xEa570105432c0fB8C0AF4B5F35d2b82eb1889e94}")
    public void setVaultContract(String address) {
        this.vaultContract = ContractUtil.addressDeserialize(address);
    }

    public String calculateUserHash(String address, BigDecimal amount) {
        final StringBuilder result = new StringBuilder();
        result.append(/*TypeEncoder.encode(new Address(*/address/*))*/);
        result.append(TypeEncoder.encode(new Uint(amount.toBigInteger())));
        return Hash.sha3(result.toString());
    }

    public String calculateParentHash(String left, String right) {
        final StringBuilder result = new StringBuilder();
        if ((new BigInteger(left.substring(2), 16)).compareTo((new BigInteger(right.substring(2), 16))) <= 0) {
            result.append(TypeEncoder.encode(new Bytes32(Numeric.hexStringToByteArray(left))));
            result.append(TypeEncoder.encode(new Bytes32(Numeric.hexStringToByteArray(right))));
        } else {
            result.append(TypeEncoder.encode(new Bytes32(Numeric.hexStringToByteArray(right))));
            result.append(TypeEncoder.encode(new Bytes32(Numeric.hexStringToByteArray(left))));
        }
        return Hash.sha3(result.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDailyAndDispatchReward(PaymentInfoEntity paymentInfo, List<UserDailyRewardEntity> userDailyRewardList, List<DispatchRewardEntity> dispatchReward) {
        paymentInfoService.update(paymentInfo);
        userDailyRewardService.updateSended(userDailyRewardList);
        dispatchRewardService.saveBulk(dispatchReward);
    }

    public int saveMerkleTree(List<MerkleTreeEntity> merkleTreeList, boolean force) {
        if (force) {
            int res = merkleTreeService.saveBulk(merkleTreeList);
            merkleTreeList.clear();
            return res;
        } else if (merkleTreeList.size() > 100) {
            int res = merkleTreeService.saveBulk(merkleTreeList);
            merkleTreeList.clear();
            return res;
        } else {
            return 0;
        }
    }

    int dispatchReward(PaymentInfoEntity paymentInfo) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        while (more) {
            List<UserDailyRewardEntity> userDailyRewardList = userDailyRewardService.getUnsended(from, size);
            if (userDailyRewardList.size() < size) {
                more = false;
            }
            from += userDailyRewardList.size();
            Map<Long, Set<String>> addresses = new HashMap<>();
            for (UserDailyRewardEntity userDailyReward : userDailyRewardList) {
                Long chainId = userDailyReward.getChainId();
                if (addresses.get(chainId) == null) {
                    addresses.put(chainId, new TreeSet<>());
                }
                addresses.get(chainId).add(userDailyReward.getChainAddress());
            }
            Map<Long, Map<String, BindRelationEntity>> bindRelationMap = new HashMap<>();
            for (Long chainId : addresses.keySet()) {
                Set<String> chainAddresses = addresses.get(chainId);
                ChainEntity chain = new ChainEntity();
                chain.setId(chainId);
                List<BindRelationEntity> chainBindRelationList = bindRelationService.getByChainAndAddresses(chain, chainAddresses);
                bindRelationMap.put(chainId, new HashMap<>());
                for (BindRelationEntity relation : chainBindRelationList) {
                    bindRelationMap.get(chainId).put(relation.getChainAddress(), relation);
                }
            }
            List<DispatchRewardEntity> dispatchRewardList = new ArrayList<>();
            for (UserDailyRewardEntity userDailyReward: userDailyRewardList) {
                Long chainId = userDailyReward.getChainId();
                String address = userDailyReward.getChainAddress();
                if (bindRelationMap.get(chainId) == null || bindRelationMap.get(chainId).get(address) == null) {
                    continue;
                }
                DispatchRewardEntity dispatchReward = new DispatchRewardEntity();
                dispatchReward.setAddress(bindRelationMap.get(chainId).get(address).getBindAddress());
                dispatchReward.setDate(paymentInfo.getDate());
                dispatchReward.setTotalAmount(userDailyReward.getReward().subtract(userDailyReward.getSended()));
                dispatchRewardList.add(dispatchReward);
                userDailyReward.setSended(userDailyReward.getReward().subtract(userDailyReward.getSended()));
                userDailyReward.setSendAddress(bindRelationMap.get(chainId).get(address).getBindAddress());
                paymentInfo.setDispatchReward(paymentInfo.getDispatchReward().add(dispatchReward.getTotalAmount()));
            }
            saveDailyAndDispatchReward(paymentInfo, userDailyRewardList, dispatchRewardList);
        }
        return 0;
    }

    String createMerkleTree(PaymentInfoEntity paymentInfo) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        List<List<String>> merkleTree = new ArrayList<>();
        merkleTree.add(new ArrayList<>());
        List<String> leaves = merkleTree.get(0);
        while (more) {
            List<DispatchRewardEntity> dispatchRewardList = dispatchRewardService.getByDate(paymentInfo.getDate(), from, size);
            if (dispatchRewardList.size() < from) {
                more = false;
            }
            from += dispatchRewardList.size();
            for (DispatchRewardEntity dispatchReward : dispatchRewardList) {
                String hash = calculateUserHash(dispatchReward.getAddress(), dispatchReward.getTotalAmount());
                dispatchReward.setHash(hash);
                leaves.add(hash); 
            }
            dispatchRewardService.updateHash(dispatchRewardList);
        }
        if (leaves.size() % 2 != 0) {
            leaves.add(Constant.EMPTY_HASH);
        }
        List<String> currentHeight = merkleTree.get(0);
        List<MerkleTreeEntity> merkleTreeList = new ArrayList<>();
        while (currentHeight.size() > 1) {
            List<String> nextHeight = new ArrayList<>();  
            merkleTree.add(nextHeight);
            for (int i = 0; i < currentHeight.size(); i += 2) {
                String parentHash = calculateParentHash(currentHeight.get(i), currentHeight.get(i + 1));
                nextHeight.add(parentHash);
                MerkleTreeEntity left = new MerkleTreeEntity();
                left.setDate(paymentInfo.getDate());
                left.setHash(currentHeight.get(i));
                left.setBrotherHash(currentHeight.get(i + 1));
                left.setParentHash(parentHash);
                merkleTreeList.add(left);
                MerkleTreeEntity right = new MerkleTreeEntity();
                right.setDate(paymentInfo.getDate());
                right.setHash(currentHeight.get(i + 1));
                right.setBrotherHash(currentHeight.get(i));
                right.setParentHash(parentHash);
                merkleTreeList.add(right);
                saveMerkleTree(merkleTreeList, false);
            }
            if (nextHeight.size() >= 2 && nextHeight.size() % 2 != 0) {
                nextHeight.add(Constant.EMPTY_HASH);
            }
            currentHeight = nextHeight;
        }
        saveMerkleTree(merkleTreeList, true);
        if (merkleTree.isEmpty() || merkleTree.get(merkleTree.size() - 1).isEmpty()) {
            return Constant.EMPTY_HASH;
        }
        return merkleTree.get(merkleTree.size() - 1).get(0);
    }

    int createTransaction(PaymentInfoEntity paymentInfo) {
        BuildTransactionEntity buildTransaction = new BuildTransactionEntity();
        buildTransaction.setFrom(vaultCaller);
        buildTransaction.setTo(vaultContract);
        buildTransaction.setValue(BigDecimal.ZERO);
        String inputDesc = "updateRooHash(" + paymentInfo.getRootHash() + "," + paymentInfo.getDispatchReward().setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "," + paymentInfo.getStartSeconds() + ")";
        buildTransaction.setInputDesc(inputDesc);
        String input = vaultContractService.updateRootHash(paymentInfo.getRootHash(), paymentInfo.getDispatchReward(), paymentInfo.getStartSeconds());
        /*
        buildTransaction.setInput(input);
        buildTransaction.setStatus(Constant.BUILD_TRANSACTION_STATUS_INIT);
        return buildTransactionService.save(buildTransaction);
        */
        return 1;
    }

    public void doPayment(PaymentInfoEntity paymentInfo) {
        int res = dispatchReward(paymentInfo);
        log.info("dispatchReward finish paymentInfo={} res={}", paymentInfo, res);
        String rootHash = createMerkleTree(paymentInfo);
        log.info("createMerkleTree finish paymentInfo={} res={}", paymentInfo, res);
        res = createTransaction(paymentInfo);
        if (res != 1) {
            throw new RuntimeException("another payment is processing");
        }
        res = paymentInfoService.updateRootHash(paymentInfo.getDate(), rootHash);
        if (res != 1) {
            throw new RuntimeException("another payment is processing");
        }
    }
}
