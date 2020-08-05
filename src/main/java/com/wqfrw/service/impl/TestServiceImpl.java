package com.wqfrw.service.impl;

import com.framework.annotation.MyService;
import com.wqfrw.service.ITestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MyService
public class TestServiceImpl implements ITestService {

    @Override
    public String query(String name, Integer id) {
        System.err.println("这是在业务方法中打印的  接口地址: /test/query");
        return "hello  " + name + ",id=" + id + "!";
    }

    @Override
    public String getException() throws Exception {
        System.err.println("这是在业务方法中打印的  接口地址: /test/exception");
        throw new Exception("这根芙蓉王是假的!");
    }

    @Override
    public String queryPage(String name) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.err.println("这是在业务方法中打印的  接口地址: /test/queryPage");
        return df.format(LocalDateTime.now());
    }

}
