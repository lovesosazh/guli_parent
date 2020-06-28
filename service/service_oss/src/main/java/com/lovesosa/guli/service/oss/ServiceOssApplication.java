package com.lovesosa.guli.service.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author lovesosa
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan({"com.lovesosa.guli"})
public class ServiceOssApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceOssApplication.class,args);
    }
}
