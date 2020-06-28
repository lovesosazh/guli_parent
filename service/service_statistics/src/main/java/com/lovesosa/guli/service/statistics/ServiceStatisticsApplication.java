package com.lovesosa.guli.service.statistics;

import com.lovesosa.guli.service.base.config.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lovesosa
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class} )
@ComponentScan(value = {"com.lovesosa.guli"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {RedisConfig.class}))
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class ServiceStatisticsApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceStatisticsApplication.class,args);
    }
}
