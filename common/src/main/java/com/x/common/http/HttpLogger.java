package com.x.common.http;

import okhttp3.logging.HttpLoggingInterceptor;
import lombok.extern.slf4j.Slf4j;

import com.x.common.constants.Constant;

@Slf4j
public class HttpLogger implements HttpLoggingInterceptor.Logger {

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Override
    public void log(String message) {
        log.info(message.length() > Constant.MAX_LOG_LENGTH ? message.substring(0, Constant.MAX_LOG_LENGTH): message);
    }
}
