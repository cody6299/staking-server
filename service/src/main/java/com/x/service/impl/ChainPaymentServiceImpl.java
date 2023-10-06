package com.x.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.*;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.dao.po.staking.*;
import com.x.common.utils.*;
import com.x.service.*;

@Slf4j
@Service
public class ChainPaymentServiceImpl implements ChainPaymentService {

    @Resource
    VoteRecordService voteRecordService;
    @Resource
    VoteRecordProcessService voteRecordProcessService;
    @Resource
    ChainUserService chainUserService;
    @Resource
    ChainService chainService;
    @Resource
    PaymentInfoService paymentInfoService;
    @Resource
    UserDailyPaymentService userDailyPaymentService;
    @Resource
    UserPaymentService userPaymentService;
    @Resource
    UserDailyRewardService userDailyRewardService;

    void updateChain(ChainEntity chain, Long currentSeconds) {
        if (chain.getLastUpdate() >= currentSeconds) {
            return;
        }
        Long lastUpdate = chain.getLastUpdate();
        chain.setLastUpdate(currentSeconds);
        if (chain.getTotalShares().compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        Long missedSeconds = currentSeconds - lastUpdate;
        BigDecimal missedReward = chain.getRewardPerSeconds().multiply(new BigDecimal(missedSeconds));
        BigDecimal missedAccPerShare = missedReward.divide(chain.getTotalShares(), Constant.ACC_SCALE, BigDecimal.ROUND_DOWN);
        chain.setAccPerShare(chain.getAccPerShare().add(missedAccPerShare));
    }

    void updateUser(ChainEntity chain, ChainUserEntity user, BigDecimal missedShares) {
        BigDecimal userPendingReward = user.getShares().multiply(chain.getAccPerShare()).subtract(user.getDebt());
        user.setReward(user.getReward().add(userPendingReward));
        user.setShares(user.getShares().add(missedShares));
        user.setDebt(user.getShares().multiply(chain.getAccPerShare()));
        chain.setTotalShares(chain.getTotalShares().add(missedShares));
    }

    void updateChainAndUser(PaymentInfoEntity paymentInfo, ChainEntity chain, ChainUserEntity user, VoteRecordEntity record, Set<String> addresses) {
        updateChain(chain, record.getVoteAt());
        updateUser(chain, user, record.getVoteNum().subtract(user.getShares()));
        paymentInfo.setCurrentRecordId(record.getId());
        addresses.add(record.getChainAddress());
    }

    @Transactional(rollbackFor = Exception.class)
    void saveChainAndUsers(PaymentInfoEntity paymentInfo, ChainEntity chain, List<ChainUserEntity> users) {
        int res = paymentInfoService.update(paymentInfo);
        if (res != 1) {
            throw new RuntimeException("another payment is processing");
        }
        chainService.save(chain);
        chainUserService.saveBulk(users);
    }

    @Transactional(rollbackFor = Exception.class)
    void savePaymentAndReward(List<UserDailyPaymentEntity> userDailyPaymentList, List<UserDailyRewardEntity> userDailyRewardList) {
        userDailyPaymentService.saveBulk(userDailyPaymentList);
        userDailyRewardService.saveBulk(userDailyRewardList);
    }

    @Transactional(rollbackFor = Exception.class)
    void updatePaymentAndDailyPayment(List<UserPaymentEntity> userPaymentList, List<UserDailyPaymentEntity> userDailyPaymentList) {
        userPaymentService.saveBulk(userPaymentList);
        userDailyPaymentService.saveBulk(userDailyPaymentList);
    }

    int dealVoteRecord(PaymentInfoEntity paymentInfo, ChainEntity chain) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        final long beginTime = DateUtil.getBeginTime(paymentInfo.getDate());
        final long endTime = DateUtil.getEndTime(paymentInfo.getDate());
        VoteRecordProcessEntity voteRecordProcessEntity = voteRecordProcessService.getByChain(chain);
        Set<String> newAddresses = new TreeSet<>();
        while (more) {
            List<VoteRecordEntity> records = voteRecordService.getByChainAndProcessAndVoteAt(chain, voteRecordProcessEntity.getBlockNum(), beginTime, endTime, from, size);
            if (records.size() < size) {
                more = false;
            }
            from += records.size();
            if (records.isEmpty()) {
                break;
            }
            Set<String> addresses = records.stream().map(VoteRecordEntity::getChainAddress).collect(Collectors.toSet());
            List<ChainUserEntity> chainUserList = chainUserService.getByChainAndAddresses(chain, addresses);
            Map<String, ChainUserEntity> chainUserMap = chainUserList.stream().collect(Collectors.toMap(ChainUserEntity::getChainAddress, user -> user));
            for (VoteRecordEntity record : records) {
                ChainUserEntity user = chainUserMap.get(record.getChainAddress());
                if (user == null) {
                    user = new ChainUserEntity();
                    user.setChainId(chain.getId());
                    user.setChainAddress(record.getChainAddress());
                    user.setShares(BigDecimal.ZERO);
                    user.setReward(BigDecimal.ZERO);
                    user.setDebt(BigDecimal.ZERO);
                    chainUserList.add(user);
                    chainUserMap.put(user.getChainAddress(), user);
                }
                updateChainAndUser(paymentInfo, chain, user, record, newAddresses);
            }
            saveChainAndUsers(paymentInfo, chain, chainUserList);
        }
        return newAddresses.size();
    }

    int syncChainAndUser(PaymentInfoEntity paymentInfo, ChainEntity chain) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        while (more) {
            List<ChainUserEntity> users = chainUserService.getByChain(chain, from, size);
            if (users.size() < size) {
                more = false;
            }
            from += users.size();
            for (ChainUserEntity user : users) {
                updateChain(chain, paymentInfo.getEndSeconds());
                updateUser(chain, user, BigDecimal.ZERO);    
            }
            saveChainAndUsers(paymentInfo, chain, users);
        }
        return from;
    }

    int calculateDailyPayment(PaymentInfoEntity paymentInfo, ChainEntity chain) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        while (more) {
            List<UserDailyPaymentEntity> toSaveUserDailyPaymentList = new ArrayList<>();
            List<ChainUserEntity> users = chainUserService.getByChain(chain, from, size);
            if (users.size() < size) {
                more = false;
            }
            from += users.size();
            Set<String> addresses = new TreeSet<String>();
            for (ChainUserEntity user: users) {
                addresses.add(user.getChainAddress()); 
            }
            List<UserPaymentEntity> userPaymentList = userPaymentService.getByChainAndAddresses(chain,  addresses);
            Map<String, UserPaymentEntity> userPaymentMap = userPaymentList.stream().collect(Collectors.toMap(UserPaymentEntity::getChainAddress, user -> user));
            for (ChainUserEntity user: users) {
                UserPaymentEntity userPayment = userPaymentMap.get(user.getChainAddress()); 
                UserDailyPaymentEntity currentDailyPayment = new UserDailyPaymentEntity();
                currentDailyPayment.setChainId(user.getChainId());
                currentDailyPayment.setChainAddress(user.getChainAddress());
                currentDailyPayment.setDate(paymentInfo.getDate());
                currentDailyPayment.setTotalReward(user.getReward());
                if (userPayment == null) {
                    currentDailyPayment.setDailyReward(user.getReward());
                    userPayment = new UserPaymentEntity();
                    userPayment.setChainId(chain.getId());
                    userPayment.setChainAddress(user.getChainAddress());
                    userPayment.setTotalReward(user.getReward());
                    userPaymentList.add(userPayment);
                    userPaymentMap.put(user.getChainAddress(), userPayment);
                } else {
                    currentDailyPayment.setDailyReward(user.getReward().subtract(userPayment.getTotalReward()));
                    userPayment.setTotalReward(user.getReward());
                }
                BigDecimal unlockReward = currentDailyPayment.getDailyReward().multiply(Constant.REWARD_UNLOCK_PERCENT);
                currentDailyPayment.setRemainRelease(currentDailyPayment.getDailyReward().subtract(unlockReward));
                currentDailyPayment.setDailyRelease(currentDailyPayment.getRemainRelease().divide(Constant.REWARD_LOCK_DAYS, 0, BigDecimal.ROUND_DOWN));
                toSaveUserDailyPaymentList.add(currentDailyPayment);
            }
            updatePaymentAndDailyPayment(userPaymentList, toSaveUserDailyPaymentList);
        }
        return from;
    }

    int calculateDailyReward(PaymentInfoEntity paymentInfo, ChainEntity chain) {
        int from = 0;
        final int size = Constant.DB_FETCH_NUM;
        boolean more = true;
        while (more) {
            List<UserDailyRewardEntity> userDailyRewardList = new ArrayList<>();
            List<UserDailyPaymentEntity> userDailyPaymentList = userDailyPaymentService.getRemainedByChain(chain, from, size);
            if (userDailyPaymentList.size() < size) {
                more = false;
            }
            from += userDailyPaymentList.size();
            for (UserDailyPaymentEntity userDailyPayment : userDailyPaymentList) {
                UserDailyRewardEntity userDailyReward = new UserDailyRewardEntity();
                userDailyReward.setChainId(chain.getId());
                userDailyReward.setChainAddress(userDailyPayment.getChainAddress());
                userDailyReward.setDate(paymentInfo.getDate());
                if (userDailyPayment.getDate() == paymentInfo.getDate()) {
                    userDailyReward.setReward(userDailyPayment.getDailyReward().subtract(userDailyPayment.getRemainRelease()));
                } else {
                    BigDecimal reward = userDailyPayment.getDailyRelease().compareTo(userDailyPayment.getRemainRelease()) >= 0 ? userDailyPayment.getRemainRelease() : userDailyPayment.getDailyRelease();
                    userDailyReward.setReward(reward);
                    userDailyPayment.setRemainRelease(userDailyPayment.getRemainRelease().subtract(reward));
                }
                userDailyReward.setSended(BigDecimal.ZERO);
                userDailyRewardList.add(userDailyReward);
            }
            savePaymentAndReward(userDailyPaymentList, userDailyRewardList);
        }
        return from;
    }

    public void doChainPayment(PaymentInfoEntity paymentInfo, ChainEntity chain) {
        int res = dealVoteRecord(paymentInfo, chain);
        log.info("dealVoteRecord finish paymentInfo={} chain={} res={}", paymentInfo, chain, res);
        res = syncChainAndUser(paymentInfo, chain);
        log.info("syncChainAndUser finish paymentInfo={} chain={} res={}", paymentInfo, chain, res);
        res = calculateDailyPayment(paymentInfo, chain);
        log.info("calculateDailyPayment finish paymentInfo={} chain={} res={}", paymentInfo, chain, res);
        res = calculateDailyReward(paymentInfo, chain);
        log.info("calculateDailyReward finish paymentInfo={} chain={} res={}", paymentInfo, chain, res);
    }
}
