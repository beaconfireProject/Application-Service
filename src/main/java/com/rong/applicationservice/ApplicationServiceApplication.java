package com.rong.applicationservice;

import com.rong.applicationservice.config.EnvLoader;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@Slf4j
public class ApplicationServiceApplication {

    public static void main(String[] args) {
        EnvLoader.init();
//        log.info(System.getProperty("jwt.secret"));
        SpringApplication.run(ApplicationServiceApplication.class, args);
    }

}
