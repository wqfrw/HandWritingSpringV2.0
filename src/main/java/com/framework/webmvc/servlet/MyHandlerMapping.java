package com.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @ClassName MyHandlerMapping
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author 我恰芙蓉王
 * @Date 2020年08月04日 15:02
 * @Version 2.0.0
 **/

public class MyHandlerMapping {

    /**
     * 请求路径校验
     */
    private Pattern pattern;

    /**
     * 实例对象
     */
    private Object controller;

    /**
     * 处理方法
     */
    private Method method;

    public MyHandlerMapping(Pattern pattern, Object instance, Method method) {
        this.pattern = pattern;
        this.controller = instance;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
