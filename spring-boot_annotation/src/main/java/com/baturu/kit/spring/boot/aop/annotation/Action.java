package com.baturu.kit.spring.boot.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuran on 2016/12/11.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name();
}
