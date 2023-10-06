package com.x.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Hash;
import java.math.*;
import org.web3j.abi.*;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.model.bean.*;
import com.x.model.vo.*;
import com.x.service.*;
import com.x.source.*;

@Slf4j
@Service
public class ERC20ContractServiceImpl implements ERC20ContractService {
    
    @Resource
    GethSource gethSource;

    public TokenInfoBean getTokenInfo(String address) {
        List<EthCallRequest> ethCallList = new ArrayList<>();
        EthCallRequest nameRequest = new EthCallRequest();
        nameRequest.setFrom(address);
        nameRequest.setTo(address);
        Function nameFunction = new Function(
            "name",
            Arrays.asList(),
            Arrays.asList(new TypeReference<Utf8String>(){})
        );
        nameRequest.setData(FunctionEncoder.encode(nameFunction));
        ethCallList.add(nameRequest);

        EthCallRequest symbolRequest = new EthCallRequest();
        symbolRequest.setFrom(address);
        symbolRequest.setTo(address);
        Function symbolFunction = new Function(
            "symbol",
            Arrays.asList(),
            Arrays.asList(new TypeReference<Utf8String>(){})
        );
        symbolRequest.setData(FunctionEncoder.encode(symbolFunction));
        ethCallList.add(symbolRequest);

        EthCallRequest decimalsRequest = new EthCallRequest();;
        decimalsRequest.setFrom(address);
        decimalsRequest.setTo(address);
        Function decimalsFunction = new Function(
            "decimals",
            Arrays.asList(),
            Arrays.asList(new TypeReference<Uint8>(){})
        );
        decimalsRequest.setData(FunctionEncoder.encode(decimalsFunction));
        ethCallList.add(decimalsRequest);

        EthCallRequest totalSupplyRequest = new EthCallRequest();;
        totalSupplyRequest.setFrom(address);
        totalSupplyRequest.setTo(address);
        Function totalSupplyFunction = new Function(
            "totalSupply",
            Arrays.asList(),
            Arrays.asList(new TypeReference<Uint256>(){})
        );
        totalSupplyRequest.setData(FunctionEncoder.encode(totalSupplyFunction));
        ethCallList.add(totalSupplyRequest);

        List<String> responses = gethSource.callBulk(ethCallList);

        TokenInfoBean tokenInfo = new TokenInfoBean();

        List<Type> nameResponse = FunctionReturnDecoder.decode(responses.get(0), nameFunction.getOutputParameters());
        tokenInfo.setName((String) nameResponse.get(0).getValue());
        List<Type> symbolResponse = FunctionReturnDecoder.decode(responses.get(1), symbolFunction.getOutputParameters());
        tokenInfo.setSymbol((String) symbolResponse.get(0).getValue());
        List<Type> decimalsResponse = FunctionReturnDecoder.decode(responses.get(2), decimalsFunction.getOutputParameters());
        tokenInfo.setDecimals(((BigInteger) decimalsResponse.get(0).getValue()).intValue());
        List<Type> totalSupplyResponse = FunctionReturnDecoder.decode(responses.get(3), totalSupplyFunction.getOutputParameters());
        tokenInfo.setTotalSupply(new BigDecimal((BigInteger) totalSupplyResponse.get(0).getValue()));
        log.debug("tokenInfo: {}", tokenInfo);

        return tokenInfo;
    }
}
