package com.x.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import io.swagger.annotations.*;
import java.util.*;

import com.x.common.utils.*;
import com.x.model.vo.*;
import com.x.service.*;
import com.x.service.*;

@Validated
@RestController
@RequestMapping("/token")
@Slf4j
@CrossOrigin(origins = "*")
public class TokenController {
    
    @Resource
    TokenControllerService tokenControllerService;

    @PostMapping("/get-erc20-info")
    public CallResult<ERC20InfoVO> getERC20Info(@RequestBody ERC20InfoParam param) {
        try {
            return tokenControllerService.getERC20Info(param);
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

}
