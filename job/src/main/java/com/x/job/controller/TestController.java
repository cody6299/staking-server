package com.x.job.controller;

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
@RequestMapping("/test")
@Slf4j
public class TestController {
    
    @Resource
    PaymentService paymentService;

    @GetMapping("/dopayment")
    public CallResult pairInfo() {
        try {
            paymentService.doPayment(DateUtil.fromString("2020-10-13 00:00:00"));
            return CallResult.success("ok");
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

}
