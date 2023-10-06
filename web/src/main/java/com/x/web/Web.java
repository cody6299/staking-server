package com.x.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Value;
import org.bouncycastle.openssl.EncryptionException;
import org.springframework.boot.SpringApplication;
import org.springframework.util.ResourceUtils;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import java.security.PrivateKey;
import java.security.Security;
import java.io.IOException;
import java.io.*;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.x.**"})
public class Web {

    public static void main(String[] args) {
        SpringApplication.run(Web.class, args);
    }

}
