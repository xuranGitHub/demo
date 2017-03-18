package com.baturu.kit.spring.boot.scheduled;

import com.baturu.kit.spring.boot.configuration.ScheduledConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by xuran on 2016/12/14.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ScheduledConfig.class);
    }
}
