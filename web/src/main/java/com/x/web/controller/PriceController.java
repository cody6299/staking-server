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
@RequestMapping("/price")
@Slf4j
@CrossOrigin(origins = "*")
public class PriceController {
    
    @Resource
    PriceControllerService priceControllerService;

    @GetMapping("/get-prices")
    public CallResult<PriceVO> getPrices() {
        try {
            return priceControllerService.getPrices();
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

}
