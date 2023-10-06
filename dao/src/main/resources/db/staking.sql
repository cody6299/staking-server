DROP DATABASE IF EXISTS staking;
CREATE DATABASE IF NOT EXISTS staking default charset utf8;
use staking;

CREATE TABLE IF NOT EXISTS `t_chain` (
    `f_id`                  BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_chain`               CHAR(32)            NOT NULL                                                        COMMENT '链的名称',
    `f_decimals`            SMALLINT            NOT NULL                                                        COMMENT '主币的精度',
    `f_reward_per_seconds`  DECIMAL(65,0)       NOT NULL DEFAULT '0'                                            COMMENT '每秒奖励的代币数量',
    `f_total_shares`        DECIMAL(65,0)       NOT NULL DEFAULT '0'                                            COMMENT '当前池子的抵押总量',
    `f_acc_per_share`       DECIMAL(65,16)      NOT NULL DEFAULT '0'                                            COMMENT '每个share的价值',
    `f_last_update`         BIGINT UNSIGNED     NOT NULL DEFAULT '0'                                            COMMENT '上一次更新的秒数',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间'
) COMMENT='链信息' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_bind_relation` (
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中对应的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的地址',
    `f_bind_address`        VARCHAR(128)        NOT NULL                                                        COMMENT '绑定的地址',
    `f_sign_data`           TEXT                NOT NULL                                                        COMMENT '绑定请求的签名信息',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY `idx_chain`(`f_chain_address`, `f_chain_id`),
    INDEX `idx_bind`(`f_bind_address`)
) COMMENT='用户绑定关系' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_bind_relation_history` (
    `f_id`                  BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中对应的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的地址',
    `f_bind_address`  VARCHAR(128)              NOT NULL                                                        COMMENT '绑定的地址',
    `f_sign_data`           TEXT                NOT NULL                                                        COMMENT '绑定请求的签名信息',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    INDEX `idx_chain`(`f_chain_address`, `f_chain_id`),
    INDEX `idx_bind`(`f_bind_address`)
) COMMENT='用户绑定关系历史记录' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chain_user` (
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中对应的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的地址',
    `f_shares`              DECIMAL(65,0)       NOT NULL DEFAULT '0'                                            COMMENT '用户的share数',
    `f_reward`              DECIMAL(65,0)       NOT NULL DEFAULT '0'                                            COMMENT '用户的总收益',
    `f_debt`                DECIMAL(65,0)       NOT NULL DEFAULT '0'                                            COMMENT '用户的欠款',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY `idx_chain`(`f_chain_address`, `f_chain_id`)
) COMMENT='用户信息' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_vote_record` (
    `f_id`                  BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中对应的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的地址',
    `f_vote_num`            DECIMAL(65,0)       NOT NULL                                                        COMMENT '投票数量',
    `f_block_num`           BIGINT UNSIGNED     NOT NULL                                                        COMMENT '投票发生的块高',
    `f_tx_hash`             VARCHAR(128)        NOT NULL                                                        COMMENT '投票发生的交易',
    `f_vote_to`             VARCHAR(128)        NOT NULL                                                        COMMENT '投票给哪个节点',
    `f_vote_at`             BIGINT              NOT NULL                                                        COMMENT '投票发生的链上时间',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    UNIQUE `idx_chain`(`f_chain_id`, `f_chain_address`, `f_tx_hash`),
    INDEX `idx_chain_address_id`(`f_chain_address`, `f_chain_id`),
    INDEX `idx_chain_to_id`(`f_vote_to`, `f_chain_id`),
    INDEX `idx_chain_timestamp_block`(`f_chain_id`, `f_vote_at`, `f_block_num`)
) COMMENT='用户的投票记录' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_vote_record_process` (
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL PRIMARY KEY                                            COMMENT 't_chain表中对应的id',
    `f_block_num`           BIGINT UNSIGNED     NOT NULL                                                        COMMENT '当前进行到的块高',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间'
) COMMENT='用户的投票记录更新进度' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_payment_info` (
    `f_date`                DATE                NOT NULL PRIMARY KEY                                            COMMENT '出账的日期',
    `f_start_seconds`       BIGINT              NOT NULL                                                        COMMENT '账单开始的时间戳',
    `f_end_seconds`         BIGINT              NOT NULL                                                        COMMENT '账单结束的时间戳',
    `f_current_record_id`   BIGINT              NOT NULL                                                        COMMENT '当前处理到的最大id',
    `f_dispatch_reward`     DECIMAL(65,0)       NOT NULL                                                        COMMENT '根节点hash',
    `f_root_hash`           VARCHAR(128)                                                                        COMMENT '根hash',
    `f_status`              SMALLINT            NOT NULL                                                        COMMENT '当前状态 0 处理中 1 成功 2 失败',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间'
) COMMENT='每日打款信息,每天每个链一条记录' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_user_payment` (
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的id',
    `f_total_reward`        DECIMAL(65,0)       NOT NULL                                                        COMMENT '用户累计总收益',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY(`f_chain_id`, `f_chain_address`)
) COMMENT='每日的用户收益快照' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_user_daily_payment` (
    `f_id`                  BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '链上的id',
    `f_date`                DATE                NOT NULL                                                        COMMENT '收益的日期',
    `f_total_reward`        DECIMAL(65,0)       NOT NULL                                                        COMMENT '用户累计总收益',
    `f_daily_reward`        DECIMAL(65,0)       NOT NULL                                                        COMMENT '用户今日释放的总收益',
    `f_remain_release`      DECIMAL(65,0)       NOT NULL                                                        COMMENT '剩余待释放的数量',
    `f_daily_release`       DECIMAL(65,0)       NOT NULL                                                        COMMENT '每天释放的数量',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    UNIQUE `idx_chain_address_date`(`f_chain_id`, `f_chain_address`, `f_date`)
) COMMENT='每日的用户收益快照' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_user_daily_reward` (
    `f_id`                  BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_chain_id`            BIGINT UNSIGNED     NOT NULL                                                        COMMENT 't_chain表中的id',
    `f_chain_address`       VARCHAR(128)        NOT NULL                                                        COMMENT '用户的链上地址',
    `f_date`                DATE                NOT NULL                                                        COMMENT '日期',
    `f_reward`              DECIMAL(65,0)       NOT NULL                                                        COMMENT '用户的每日收益',
    `f_sended`              DECIMAL(65,0)       NOT NULL                                                        COMMENT '已经分到绑定地址的数量',
    `f_send_address`        VARCHAR(128)                                                                        COMMENT '收益分给了哪个链上地址',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    UNIQUE(`f_chain_id`, `f_chain_address`, `f_date`)
) COMMENT='每日的用户收益记录' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_dispatch_reward` (
    `f_address`             VARCHAR(128)        NOT NULL                                                        COMMENT '链上地址',
    `f_date`                DATE                NOT NULL                                                        COMMENT '日期',
    `f_total_amount`        DECIMAL(65,0)       NOT NULL                                                        COMMENT '改地址总共分发的收益',
    `f_hash`                VARCHAR(128)                                                                        COMMENT '用户收益的hash',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY(`f_address`, `f_date`)
) COMMENT='每个链上地址分发的总收益' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_merkle_tree` ( 
    `f_date`                DATE                NOT NULL                                                        COMMENT '日期',
    `f_hash`                VARCHAR(128)        NOT NULL                                                        COMMENT '用户的hash',
    `f_brother_hash`        VARCHAR(128)        NOT NULL                                                        COMMENT '兄弟hash',
    `f_parent_hash`         VARCHAR(128)        NOT NULL                                                        COMMENT '父hash',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY(`f_date`, `f_hash`),
    INDEX `idx_address_height`(`f_address`, `f_height`)
) COMMENT='最终分发收益的merkle树' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_price` (
    `f_id`                  BIGINT              NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_address`             VARCHAR(128)                                                                        COMMENT '代币的合约地址',
    `f_token`               VARCHAR(32)         NOT NULL                                                        COMMENT '代币名称',
    `f_price`               DECIMAL(65,16)      NOT NULL                                                        COMMENT '当前价格',
    `f_decimals`            SMALLINT            NOT NULL                                                        COMMENT '币种精度',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间'
) COMMENT='代币价格' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_build_transaction` (
    `f_id`                  BIGINT              NOT NULL PRIMARY KEY AUTO_INCREMENT                             COMMENT '自增id',
    `f_from`                VARCHAR(128)        NOT NULL                                                        COMMENT '代币的合约地址',
    `f_to`                  VARCHAR(128)        NOT NULL                                                        COMMENT '代币名称',
    `f_value`               DECIMAL(65,0)       NOT NULL                                                        COMMENT '转账数量',
    `f_input`               TEXT                NOT NULL                                                        COMMENT '交易数据',
    `f_input_desc`          TEXT                NOT NULL                                                        COMMENT '交易数据的可读格式',
    `f_raw_transaction`     TEXT                NOT NULL                                                        COMMENT '二进制签名交易',
    `f_status`              SMALLINT            NOT NULL                                                        COMMENT '状态',
    `f_hash`                VARCHAR(128)        NOT NULL                                                        COMMENT '交易hash',
    `f_create_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP                              COMMENT '创建时间',
    `f_update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间'
) COMMENT='要执行的交易' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO t_chain
    (f_id, f_chain, f_reward_per_seconds, f_decimals, f_total_shares, f_acc_per_share, f_last_update) 
VALUES
    (1, 'ETH', '0', 18, '0', '0', '0'),
    (2, 'DOT', '2000000000000000', 12, '0', '0', '0')
ON DUPLICATE KEY UPDATE
    f_update_at = values(f_update_at)
;

INSERT INTO t_vote_record
    (f_chain_id, f_chain_address, f_vote_num, f_block_num, f_tx_hash, f_vote_to, f_vote_at)
VALUES
    (2, '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '100000000000000', 1, '1', '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '1602547300'),
    (2, '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '100000000000000', 1, '2', '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '1602548300'),
    (2, '5FHaDS63ngSxvUzJgbLtWxzX1eHdQ4xmBd6W7DKe3W97Y1dH', '100000000000000', 1, '1', '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '1602547300'),
    (2, '5FHaDS63ngSxvUzJgbLtWxzX1eHdQ4xmBd6W7DKe3W97Y1dH', '100000000000000', 1, '2', '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '1602548300')
ON DUPLICATE KEY UPDATE
    f_vote_num = values(f_vote_num),
    f_vote_at = values(f_vote_at)
;

INSERT INTO t_vote_record_process
    (f_chain_id, f_block_num)
VALUES
    (2, 2)
ON DUPLICATE KEY UPDATE
    f_block_num = values(f_block_num)
;

INSERT INTO t_bind_relation
    (f_chain_id, f_chain_address, f_bind_address, f_sign_data)
VALUES
    (2, '5H1LeEvdS9DeyMqZQsqxFwr1HXam9V6gYEXdoWgoYeZB8hxX', '7286d8b6a41a3ce6e2360bb62ebc5b15059c2166', '123'),
    (2, '5FHaDS63ngSxvUzJgbLtWxzX1eHdQ4xmBd6W7DKe3W97Y1dH', '5c7b53292f4444674a674667887e781e4c4649d7', '123')
;

INSERT INTO t_price
    (f_id, f_address, f_decimals, f_token, f_price)
VALUES
    (2, null, 12, 'dot', '41.61'),
    (1000001, '456469b4fcd1993a734fe7cae3be039ab946ba9a', 18, 'fingo', '0.5')
;
