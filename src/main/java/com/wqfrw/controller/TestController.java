package com.wqfrw.controller;

import com.framework.annotation.MyAutowired;
import com.framework.annotation.MyController;
import com.framework.annotation.MyRequestMapping;
import com.framework.annotation.MyRequestParam;
import com.framework.webmvc.servlet.MyModelAndView;
import com.wqfrw.service.ITestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@MyController
@MyRequestMapping("/test")
public class TestController {

    @MyAutowired
    private ITestService testService;

    @MyRequestMapping("/queryPage")
    public MyModelAndView queryPage(@MyRequestParam("name") String name) {
        String result = testService.queryPage(name);
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("date", result);
        model.put("food", "老八秘制小汉堡");
        return new MyModelAndView("index.html", model);
    }

    @MyRequestMapping("/query")
    public MyModelAndView query(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name, @MyRequestParam("id") Integer id) {
        String result = testService.query(name, id);
        return out(resp,result);
    }

    @MyRequestMapping("/exception")
    public MyModelAndView getException(HttpServletRequest req,HttpServletResponse resp) throws Exception {
        String result = testService.getException();
        return out(resp,result);
    }

    @MyRequestMapping("/add")
    public MyModelAndView add(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name) {
        String result = "hello " + name + " , AddInterfaceInDevelopment...";
        return out(resp,result);
    }

    @MyRequestMapping("/remove")
    public MyModelAndView remove(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name) {
        String result = "hello " + name + " , DeleteInterfaceInDevelopment...";
        return out(resp,result);
    }

    private MyModelAndView out(HttpServletResponse resp, String outValue) {
        try {
            resp.getWriter().write(outValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
