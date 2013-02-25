
package com.nebhale.cyclinglibrary.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
class UtilConfiguration {

    @Bean
    ScheduledExecutorService executorService() {
        return Executors.newScheduledThreadPool(5, new CustomizableThreadFactory("point-augmenter-"));
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
