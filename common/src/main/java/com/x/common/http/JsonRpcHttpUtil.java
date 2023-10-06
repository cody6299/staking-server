package com.x.common.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.github.arteam.simplejsonrpc.client.JsonRpcClient;
import com.github.arteam.simplejsonrpc.client.builder.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import java.util.*;
import okhttp3.*;

@Slf4j
public class JsonRpcHttpUtil {

    @Data
    public static class RpcConfig {
        String rpcUrl;
        String rpcUser;
        String rpcPassword;

        @Override
        public int hashCode() {
            return rpcUrl.hashCode() | rpcUser.hashCode() | rpcPassword.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RpcConfig other = (RpcConfig) obj;
            if (!rpcUrl.equals(other.rpcUrl)) {
                return false;
            }
            if (!rpcUser.equals(other.rpcUser)) {
                return false;
            }
            if (!rpcPassword.equals(other.rpcPassword)) {
                return false;
            }
            return true;
        }
    }

    static Map<RpcConfig, JsonRpcClient> clients = new HashMap<>();

    static void init(RpcConfig config) {
        if (clients.get(config) != null) {
            return;
        }

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonRpcClient client = new JsonRpcClient(request -> {
            Response res = HttpUtil.getConnection().newCall(new Request.Builder()
                .url(config.getRpcUrl())
                .addHeader("Authorization", Credentials.basic(config.getRpcUser(), config.getRpcPassword()))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), request))
                .build())
                .execute();
            return res.body().string();
        }, om);

        clients.put(config, client);
    }
    

    public static RequestBuilder<Object> build(RpcConfig config) {
        init(config);
        return clients.get(config).createRequest();
    }

    public static BatchRequestBuilder<?, ?> buildBatch(RpcConfig config) {
        init(config);
        return clients.get(config).createBatchRequest();
    }
}
