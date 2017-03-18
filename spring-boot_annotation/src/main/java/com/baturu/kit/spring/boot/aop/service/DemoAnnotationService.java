package com.baturu.kit.spring.boot.aop.service;


import com.baturu.kit.spring.boot.aop.annotation.Action;
import org.springframework.stereotype.Service;

/**
 * Created by xuran on 2016/12/11.
 */
@Service
public class DemoAnnotationService {
    @Action(name = "注解式拦截的add操作")
    public void add() {}
}
