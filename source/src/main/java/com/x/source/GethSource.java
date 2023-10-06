package com.x.source;

import org.springframework.beans.factory.annotation.Value;
import com.github.arteam.simplejsonrpc.client.builder.*;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import com.x.common.http.*;
import com.x.model.bean.*;
import java.util.*;

@Component
public class GethSource {
    @Value("${source.geth.url:https://data-seed-prebsc-1-s1.binance.org:8545}")
    String url;
    @Value("${source.geth.user:}")
    String user;
    @Value("${source.geth.password:}")
    String password;
    JsonRpcHttpUtil.RpcConfig rpcConfig;
    @PostConstruct
    void init() {
        rpcConfig = new JsonRpcHttpUtil.RpcConfig();
        rpcConfig.setRpcUrl(url);
        rpcConfig.setRpcUser(user);
        rpcConfig.setRpcPassword(password);
    }

    public String call(EthCallRequest param) {
        String res = JsonRpcHttpUtil.build(rpcConfig)
                .id(5)
                .method("eth_call")
                .params(param, "latest")
                .returnAs(String.class)
                .execute();
        return res;
    }

    public List<String> callBulk(List<EthCallRequest> params) {
        BatchRequestBuilder<?, ?> builder = JsonRpcHttpUtil.buildBatch(rpcConfig);
        for (int i = 0; i < params.size(); i++) {
            EthCallRequest param = params.get(i);
            builder.add(i, "eth_call", param, "latest");
        }
        Map<Integer, String> responses = builder
                .keysType(Integer.class)
                .returnType(String.class)
                .execute();
        List<String> res = new ArrayList();
        for (Integer index : responses.keySet()) {
            res.add(index, responses.get(index));
        }
        return res;
    }

    /*
    public static String HEX_PREFIX = "0x";

    @Value("${source.huobi.eco.url:http://127.0.0.1:8545}")
    String url;
    @Value("${source.huobi.eco.address}")
    String ecoAddress;
    @Value("${source.huobi.eco.user:}")
    String user;
    @Value("${source.huobi.eco.password:}")
    String password;
    private Web3j web3j = Web3j.build(new HttpService(url));

    public Integer getTransactionCount(GetTransactionCountRequest param) {
        String hexNumber = JsonRpcHttpUtil.build(rpcConfig)
                .id(2)
                .method("eth_getTransactionCount")
                .params(String.format("0x%s", param.getAccount()), param.getPeriod())
                .returnAs(String.class)
                .execute();
        return Integer.decode(hexNumber);
    }

    public BigInteger gasPrice() {
        String result = JsonRpcHttpUtil.build(rpcConfig)
                .id(3)
                .method("eth_gasPrice")
                .params()
                .returnAs(String.class)
                .execute();
        if (result.startsWith("0x")) {
            result = result.substring(2);
        }
        return new BigInteger(result, 16);
    }

    public String broadcast(String rawTx) {
        String hash = JsonRpcHttpUtil.build(rpcConfig)
                .id(4)
                .method("eth_sendRawTransaction")
                .params(rawTx)
                .returnAs(String.class)
                .execute();
        return hash;
    }

    public List<GetLogsResponse> getLogs(GetLogsRequest param) {
        List<GetLogsResponse> response = JsonRpcHttpUtil.build(rpcConfig)
                .id(6)
                .method("eth_getLogs")
                .params(param)
                .returnAs(new TypeReference<List<GetLogsResponse>>() {
                })
                .execute();
        return response;
    }

    public EthGetTransactionReceipt getTransactionReceipt(String hash) {
        try {
            return web3j.ethGetTransactionReceipt(hash).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMaxHeight() {
        String hexNumber = JsonRpcHttpUtil.build(rpcConfig)
                .id(7)
                .method("eth_blockNumber")
                .returnAs(String.class)
                .execute();
        return Integer.decode(hexNumber);
    }

    public BlockBean getBlockByNumber(Integer height) {
        BlockBean block = JsonRpcHttpUtil.build(rpcConfig)
                .id(8)
                .method("eth_getBlockByNumber")
                .params(String.format("0x%x", height), true)
                .returnAs(BlockBean.class)
                .execute();
        return block;
    }

    public Map<String, ReceiptBean> getTransactionReceipts(List<TransactionBean> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return new HashMap<>();
        }
        BatchRequestBuilder<?, ?> builder = JsonRpcHttpUtil.buildBatch(rpcConfig);
        for (TransactionBean transaction : transactions) {
            builder.add(transaction.getHash(), "eth_getTransactionReceipt", String.format("0x%s", transaction.getHash()));
        }
        Map<String, ReceiptBean> receipts = builder
            .keysType(String.class)
            .returnType(ReceiptBean.class)
            .execute();
        return receipts;
    }

    public BigInteger getLiquiditeExchangeList(String data) {
        Transaction transaction = Transaction.createEthCallTransaction(null, HEX_PREFIX + ecoAddress, data);
        try {
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            return new BigInteger(removeOx(ethCall.getValue()), 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal getRepaymentAmount(String data) {
        Transaction transaction = Transaction.createEthCallTransaction(null, HEX_PREFIX + ecoAddress, data);
        try {
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            return new BigDecimal(new BigInteger(removeOx(ethCall.getValue()), 16)).divide(new BigDecimal("1000000000000000000"), 18, BigDecimal.ROUND_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal liquiditeExRepayBorrow(String data) {
        Transaction transaction = Transaction.createEthCallTransaction(null, HEX_PREFIX + ecoAddress, data);
        try {
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            return new BigDecimal(new BigInteger(removeOx(ethCall.getValue()), 16)).divide(new BigDecimal("1000000000000000000"), 18, BigDecimal.ROUND_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static String removeOx(String input) {
        return input.startsWith(HEX_PREFIX) ? input.substring(2) : input;
    }
    */
}
