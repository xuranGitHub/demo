package com.baturu.kit.spring.boot.async.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by xuran on 2016/12/14.
 */
@Service
public class AsyncTaskService {

    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println(Thread.currentThread().getName() + "执行异步任务：" + i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i) {
        System.out.println(Thread.currentThread().getName() + "执行异步任务+1：" + i);
    }
}
