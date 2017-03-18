package com.baturu.kit.spring.boot.async;

import com.baturu.kit.spring.boot.async.service.AsyncTaskService;
import com.baturu.kit.spring.boot.configuration.AsyncConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by xuran on 2016/12/14.
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(AsyncConfig.class);
        AsyncTaskService asyncTaskService = configApplicationContext.getBean(AsyncTaskService.class);
        for (int i = 0; i < 10; i++) {
            asyncTaskService.executeAsyncTask(i);
            asyncTaskService.executeAsyncTaskPlus(i);
        }
        configApplicationContext.close();
    }
}