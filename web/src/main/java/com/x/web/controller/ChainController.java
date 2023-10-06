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
@RequestMapping("/chain")
@Slf4j
@CrossOrigin(origins = "*")
public class ChainController {
    
    @Resource
    ChainControllerService chainControllerService;

    @GetMapping("/get-chains")
    public CallResult<ChainVO> getPrices() {
        try {
            return chainControllerService.getChains();
        } catch(Exception ex) {
            log.error("caught exception: ", ex);
            return CallResult.failure(ex.toString());
        }
    }

}
