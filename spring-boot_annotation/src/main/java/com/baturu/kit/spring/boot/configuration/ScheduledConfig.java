package com.baturu.kit.spring.boot.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by xuran on 2016/12/14.
 */
@Configuration
@ComponentScan("com.baturu.kit.spring.boot.scheduled.service")
@EnableScheduling
public class ScheduledConfig {
}
