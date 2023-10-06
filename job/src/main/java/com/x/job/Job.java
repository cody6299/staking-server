package com.x.job;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.x.**"})
public class Job {

    public static void main(String[] args) {
        SpringApplication.run(Job.class, args);
    }

}
