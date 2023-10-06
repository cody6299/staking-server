package com.x.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.model.bean.*;
import com.x.model.vo.*;
import com.x.service.*;

@Slf4j
@Service
public class UserControllerServiceImpl implements UserControllerService {
    
    @Resource
    BindRelationService bindRelationService;
    @Resource
    BindRelationHistoryService bindRelationHistoryService;
    @Resource
    ChainUserService chainUserService;
    @Resource
    PriceService priceService;
    @Resource
    UserPaymentService userPaymentService;
    @Resource
    UserDailyRewardService userDailyRewardService;
    @Resource
    DispatchRewardService dispatchRewardService;
    @Resource
    MerkleTreeService merkleTreeService;

    @Transactional(rollbackFor = Exception.class)
    void saveBindRelation(BindRelationEntity bindRelation, BindRelationHistoryEntity bindRelationHistory, String oldBindAddress) {
        if (bindRelationHistory != null) {
            bindRelationHistoryService.save(bindRelationHistory);
            int res = bindRelationService.update(bindRelation, oldBindAddress);
            if (res != 1) {
                throw new RuntimeException("already updated"); 
            }
        } else {
            bindRelationService.save(bindRelation);
        }
    }

    public CallResult<UserProfitVO> getUserProfit(UserProfitParam param) {
        UserProfitVO res = new UserProfitVO();
        List<PriceEntity> prices = priceService.getAll();
        Map<Long, PriceEntity> priceMap = prices.stream().collect(Collectors.toMap(PriceEntity::getId, price -> price));
        Integer rewardTokenDecimals = priceMap.get(Constant.TOKEN_ID_FINGO).getDecimals();
        for (UserProfitParam.ChainUser user : param.getUsers()) {
            Long chainId = user.getChainId();
            UserProfitVO.Profit profit = new UserProfitVO.Profit();
            Integer decimals = priceMap.get(chainId).getDecimals();
            profit.setChainId(chainId);
            res.getProfits().add(profit);
            ChainEntity chain = new ChainEntity();
            chain.setId(chainId);
            String chainAddress = user.getChainAddress();
            List<ChainUserEntity> chainUserList = chainUserService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(chainAddress)));
            if (chainUserList == null || chainUserList.isEmpty()) {
                profit.setStakedAmount(BigDecimal.ZERO);
                profit.setStakedValue(BigDecimal.ZERO);
            } else {
                ChainUserEntity chainUser = chainUserList.get(0);
                profit.setStakedAmount(chainUser.getShares().divide(BigDecimal.TEN.pow(decimals)));
                profit.setStakedValue(profit.getStakedAmount().multiply(priceMap.get(chainId).getPrice()));
            }
            List<UserPaymentEntity> userPaymentList = userPaymentService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(chainAddress)));
            if (userPaymentList == null || userPaymentList.isEmpty()) {
                profit.setEarnedToken(BigDecimal.ZERO);
            } else {
                UserPaymentEntity userPayment = userPaymentList.get(0);
                profit.setEarnedToken(userPayment.getTotalReward().divide(BigDecimal.TEN.pow(rewardTokenDecimals)));
            }
            BigDecimal totalUnlocked = userDailyRewardService.getTotalSendedByChanAndUserLessDate(chain, chainAddress, param.getDate());
            if (totalUnlocked == null) {
                profit.setUnlockedToken(BigDecimal.ZERO);
            } else {
                profit.setUnlockedToken(totalUnlocked.divide(BigDecimal.TEN.pow(rewardTokenDecimals)));
            }
            profit.setLockedToken(profit.getEarnedToken().subtract(profit.getUnlockedToken()));
        }
        return CallResult.success(res);
    }

    public CallResult<BindAddressVO> bindAddress(BindAddressParam param) {
        BindAddressVO res = new BindAddressVO();
        res.setChainId(param.getChainId());
        res.setChainAddress(param.getChainAddress());
        res.setBindAddress(param.getBindAddress());
        ChainEntity chain = new ChainEntity();
        chain.setId(param.getChainId());
        List<BindRelationEntity> currentBindRelationList = bindRelationService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(param.getChainAddress())));
        BindRelationHistoryEntity bindRelationHistory = null;
        BindRelationEntity currentBindRelation = null;
        String oldBindAddress = null;
        if (currentBindRelationList != null && !currentBindRelationList.isEmpty()) {
            currentBindRelation = currentBindRelationList.get(0);
            oldBindAddress = currentBindRelation.getBindAddress();
            if (oldBindAddress.equals(param.getBindAddress())) {
                return CallResult.success(res);
            }
            bindRelationHistory = new BindRelationHistoryEntity();
            bindRelationHistory.setChainId(currentBindRelation.getChainId());
            bindRelationHistory.setChainAddress(currentBindRelation.getChainAddress());
            bindRelationHistory.setBindAddress(currentBindRelation.getBindAddress());
            bindRelationHistory.setSignData(currentBindRelation.getSignData());
            currentBindRelation.setBindAddress(param.getBindAddress());
            currentBindRelation.setSignData(param.getSignature());
        } else {
            currentBindRelation = new BindRelationEntity();
            currentBindRelation.setChainId(param.getChainId());
            currentBindRelation.setChainAddress(param.getChainAddress());
            currentBindRelation.setBindAddress(param.getBindAddress());
            currentBindRelation.setSignData(param.getSignature());
        }
        saveBindRelation(currentBindRelation, bindRelationHistory, oldBindAddress);
        return CallResult.success(res);
    }

    public CallResult<BindRelationVO> getBindRelation(BindRelationParam param) {
        BindRelationVO res = new BindRelationVO();
        for (int i = 0; i < param.getUsers().size(); i ++) {
            BindRelationParam.ChainUser user = param.getUsers().get(i);
            ChainEntity chain = new ChainEntity();
            chain.setId(user.getChainId());
            List<BindRelationEntity> bindRelationList = bindRelationService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(user.getChainAddress())));
            if (bindRelationList == null || bindRelationList.isEmpty()) {
                continue;
            }
            BindRelationEntity bindRelation = new BindRelationEntity();
            BindRelationVO.Relation relation = new BindRelationVO.Relation();
            relation.setChainId(user.getChainId());
            relation.setChainAddress(user.getChainAddress());
            relation.setBindAddress(bindRelation.getBindAddress());
            List<BindRelationHistoryEntity> bindRelationHistoryList = bindRelationHistoryService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(user.getChainAddress())));
            for (BindRelationHistoryEntity bindRelationHistory : bindRelationHistoryList) {
                BindRelationVO.History history = new BindRelationVO.History();
                history.setBindAddress(bindRelationHistory.getBindAddress());
                history.setCreateAt(bindRelationHistory.getCreateAt().getTime());
                relation.getBindHistory().add(history);
            }
            res.getRelations().add(relation);
        }
        return CallResult.success(res);
    }

    public CallResult<UserClaimVO> getUserClaim(UserClaimParam param) {
        UserClaimVO res = new UserClaimVO();
        for (int i = 0; i < param.getUsers().size(); i ++) {
            UserClaimParam.ChainUser user = param.getUsers().get(i);
            Long chainId = user.getChainId();
            String chainAddress = user.getChainAddress();
            ChainEntity chain = new ChainEntity();
            chain.setId(chainId);
            List<BindRelationEntity> bindRelationList = bindRelationService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(user.getChainAddress())));
            List<BindRelationHistoryEntity> bindRelationHistoryList = bindRelationHistoryService.getByChainAndAddresses(chain, new TreeSet<String>(Arrays.asList(user.getChainAddress())));
            Set<String> addresses = new TreeSet<String>();
            for (BindRelationEntity bindRelation : bindRelationList) {
                addresses.add(bindRelation.getBindAddress());
            }
            for (BindRelationHistoryEntity bindRelationHistory : bindRelationHistoryList) {
                addresses.add(bindRelationHistory.getBindAddress());
            }
            UserClaimVO.ChainUser userVO = new UserClaimVO.ChainUser();
            res.getChains().add(userVO);
            userVO.setChainId(chainId);
            userVO.setChainAddress(chainAddress);
            List<DispatchRewardEntity> dispatchRewardList = dispatchRewardService.getByAddressesAndDate(param.getDate(), addresses);
            Map<String, DispatchRewardEntity> dispatchRewardMap = dispatchRewardList.stream().collect(Collectors.toMap(DispatchRewardEntity::getAddress, reward -> reward));
            for (String address : addresses) {
                UserClaimVO.Claim claimVO = new UserClaimVO.Claim();    
                userVO.getClaims().add(claimVO);
                claimVO.setAddress(address);
                if (dispatchRewardMap.get(address) == null) {
                    claimVO.setAmount(BigDecimal.ZERO);
                } else {
                    claimVO.setAmount(dispatchRewardMap.get(address).getTotalAmount());
                    MerkleTreeEntity tree = null;
                    String hash = dispatchRewardMap.get(address).getHash();
                    do {
                        tree = merkleTreeService.getByDateAndHash(param.getDate(), hash); 
                        if (tree != null) {
                            hash = tree.getParentHash();
                            claimVO.getProofs().add(tree.getBrotherHash());
                        }
                    } while (tree != null);
                }
            }
        }
        return CallResult.success(res);
    }
}
