package com.x.common.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import com.x.common.constants.Constant;

@Slf4j
@SpringBootConfiguration
public class HttpUtil {

    static OkHttpClient oc = null;

    static int connectTimeoutSeconds = Constant.DEFAULT_HTTP_CONNECT_TIMEOUT;
    static int readTimeoutSeconds = Constant.DEFAULT_HTTP_READ_TIMEOUT;
    static int writeTimeoutSeconds = Constant.DEFAULT_HTTP_WRITE_TIMEOUT;

    @Value("${http.connect-timeout-seconds:0}")
    public void setConnectTimeoutSeconds(int value) {
        if (value <= 0) {
            value = Constant.DEFAULT_HTTP_CONNECT_TIMEOUT;
        }
        connectTimeoutSeconds = value;
    }
    @Value("${http.read-timeout-seconds:0}")
    public void setReadTimeoutSeconds(int value) {
        if (value <= 0) {
            value = Constant.DEFAULT_HTTP_READ_TIMEOUT;
        }
        readTimeoutSeconds = value;
    }
    @Value("${http.write-timeout-seconds:0}")
    public void setWriteTimeoutSeconds(int value) {
        if (value <= 0) {
            value = Constant.DEFAULT_HTTP_WRITE_TIMEOUT;
        }
        writeTimeoutSeconds = value;
    }


    static void init() {
        if (oc != null) {
            return;
        }
        oc = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
                .addInterceptor(HttpLogger.getLoggingInterceptor())
                .build();
    }

    public static OkHttpClient getConnection() {
        init();
        return oc;
    }

}
