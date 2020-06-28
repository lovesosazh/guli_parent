package com.lovesosa.guli.service.sms;

import com.lovesosa.guli.service.base.config.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author lovesosa
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class )
@ComponentScan(value = {"com.lovesosa.guli"})
@EnableDiscoveryClient
public class ServiceSmsApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceSmsApplication.class,args);
    }
}
