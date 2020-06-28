package com.lovesosa.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lovesosa
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan({"com.lovesosa.guli"})
public class ServiceCmsApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceCmsApplication.class,args);
    }
}
