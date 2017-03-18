package com.baturu.testCode;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuran on 2016/1/28.
 */
public class TestDate {
    @Test
    public void getRecentOneMonthRange(){
        Date dateNow = new Date();
        Calendar ThreeMonthBefore = Calendar.getInstance();
        ThreeMonthBefore.setTime(dateNow);
        ThreeMonthBefore.add(Calendar.MONTH,-1);

        Date beginDate =ThreeMonthBefore.getTime();
        Date endDate = dateNow;
        System.out.println(dateNow);
    }

    @Test
    public void replace() {
        String a = "6216*0018";
        a = a.replace(a.substring(a.indexOf("*"), a.lastIndexOf("*") + 1), "***");
        System.out.println(a);
    }
}
