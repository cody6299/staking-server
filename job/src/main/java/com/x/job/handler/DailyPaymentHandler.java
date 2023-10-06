package com.x.job.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.biz.model.ReturnT;
import java.util.concurrent.TimeUnit;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import javax.annotation.Resource;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import java.io.DataOutputStream;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import org.slf4j.Logger; import java.net.URL;
import java.util.*;

import com.x.common.utils.*;
import com.x.service.*;
import com.x.model.*;

@Slf4j
@Component
public class DailyPaymentHandler {

    @Resource
    PaymentService paymentService;

    @XxlJob("dailyPaymentHandler")
    public void execute() throws Exception {
        String dateStr = XxlJobHelper.getJobParam();
        log.info("dailyPaymentHandler doPayment begin. date={}", dateStr);
	    try {
            Date date = null;
            if (dateStr != null) {
                try {
                    date = DateUtil.fromString(dateStr);
                } catch (Exception ex) {
                    date = null;
                }
            }
            if (date == null) {
                date = DateUtil.getYesterdayUTCDate();
            }
            paymentService.doPayment(date);
            log.info("dailyPaymentHandler doPayment finsih. date={}", date);
	    } catch (Exception ex) {
	        log.error("caught exception: ", ex);
            throw ex;
	    }
    }

}
