package com.baturu.kit.spring.boot.condition.service;

import com.baturu.kit.spring.boot.condition.ListService;

/**
 * Created by xuran on 2016/12/14.
 */
public class LinuxListService implements ListService {

    @Override
    public String showListCmd() {
        return "ls";
    }
}
