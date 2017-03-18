package com.baturu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by xuran on 16/2/27.
 */
@Controller
@RequestMapping("message")
public class MessageController {

    @RequestMapping("gotoList")
    public String MessageList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("9999");
        return "message/list";
    }
}
