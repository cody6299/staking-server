package com.x.common.constants;

import java.math.*;

public interface Constant {
    
    public final int MAX_LOG_LENGTH = 10240;

    public final int DEFAULT_HTTP_CONNECT_TIMEOUT = 60;
    public final int DEFAULT_HTTP_READ_TIMEOUT = 60;
    public final int DEFAULT_HTTP_WRITE_TIMEOUT = 60;
    public final int PRICE_PERCISION = 17;
    public final String EMPTY_HASH = "0x0000000000000000000000000000000000000000000000000000000000000000";
    public final int PAYMENT_STATUS_PROCESS = 0;
    public final int PAYMENT_STATUS_SUCCESS = 1;
    public final int PAYMENT_STATUS_FAILED = 2;
    public final int DB_FETCH_NUM = 100;

    public final BigDecimal REWARD_UNLOCK_PERCENT = new BigDecimal("0.2");
    public final BigDecimal REWARD_LOCK_DAYS = new BigDecimal("80");
    public final int ACC_SCALE = 16;

    public final long CHAIN_ID_ETH = 1L;
    public final long CHAIN_ID_DOT = 1L;
    public final long TOKEN_ID_FINGO = 1000001L;

}
