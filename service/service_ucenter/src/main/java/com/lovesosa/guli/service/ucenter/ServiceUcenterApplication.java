package com.lovesosa.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lovesosa
 */
@SpringBootApplication
@ComponentScan("com.lovesosa.guli")
@EnableDiscoveryClient
public class ServiceUcenterApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceUcenterApplication.class,args);
    }
}
