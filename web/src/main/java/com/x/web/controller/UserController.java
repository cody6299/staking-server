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
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {
    
    @Resource
    UserControllerService userControllerService;

    @PostMapping("/get-user-profit")
    public CallResult<UserProfitVO> getUserProfit(@RequestBody UserProfitParam param) {
        try {
            return userControllerService.getUserProfit(param);
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

    @PostMapping("/bind-address")
    public CallResult<BindAddressVO> bindAddress(@RequestBody BindAddressParam param) {
        try {
            return userControllerService.bindAddress(param);
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

    @PostMapping("/get-bind-relation")
    public CallResult<BindRelationVO> getBindRelation(@RequestBody BindRelationParam param) {
        try {
            return userControllerService.getBindRelation(param);
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }
    @PostMapping("/get-user-claim")
    public CallResult<UserClaimVO> getUserClaim(@RequestBody UserClaimParam param) {
        try {
            return userControllerService.getUserClaim(param);
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }
}
