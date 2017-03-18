package com.baturu.kit.spring.boot.aop;

import com.baturu.kit.spring.boot.aop.service.DemoAnnotationService;
import com.baturu.kit.spring.boot.aop.service.DemoMethodService;
import com.baturu.kit.spring.boot.configuration.AopConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by xuran on 2016/12/11.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(AopConfig.class);
        DemoAnnotationService demoAnnotationService =
                configApplicationContext.getBean(DemoAnnotationService.class);
        DemoMethodService demoMethodService =
                configApplicationContext.getBean(DemoMethodService.class);
        demoAnnotationService.add();
        demoMethodService.add();
        configApplicationContext.close();
    }
}
