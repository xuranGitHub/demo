package com.baturu.kit.spring.boot.configuration;

import com.baturu.kit.spring.boot.condition.LinuxCondition;
import com.baturu.kit.spring.boot.condition.ListService;
import com.baturu.kit.spring.boot.condition.MacOsCondition;
import com.baturu.kit.spring.boot.condition.service.LinuxListService;
import com.baturu.kit.spring.boot.condition.service.MacOsListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xuran on 2016/12/14.
 */
@Configuration
public class ConditionConfig {

    @Bean
    @Conditional(LinuxCondition.class)
    public ListService linuxListService() {
        return new LinuxListService();
    }

    @Bean
    @Conditional(MacOsCondition.class)
    public ListService macOsListService() {
        return new MacOsListService();
    }
}
