package com.baturu.kit.spring.boot.condition;

import com.baturu.kit.spring.boot.configuration.ConditionConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by xuran on 2016/12/14.
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);
        ListService listService = context.getBean(ListService.class);
        System.out.println(context.getEnvironment().getProperty("os.name")
                + "系统下的列表命令为： "
                + listService.showListCmd());
    }
}
