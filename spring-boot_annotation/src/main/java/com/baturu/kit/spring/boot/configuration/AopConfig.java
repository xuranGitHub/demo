package com.baturu.kit.spring.boot.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by xuran on 2016/12/11.
 */
@Configuration
@ComponentScan("com.baturu.kit.spring.boot.aop")
@EnableAspectJAutoProxy
public class AopConfig {
}
