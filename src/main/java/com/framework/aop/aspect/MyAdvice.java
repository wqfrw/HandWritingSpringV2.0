package com.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @ClassName MyAdvice
 * @Description TODO(通知)
 * @Author 我恰芙蓉王
 * @Date 2020年08月05日 10:24
 * @Version 2.0.0
 **/

public class MyAdvice {

    /**
     * 通知对象
     */
    private Object aspect;

    /**
     * 通知方法
     */
    private Method adviceMethod;

    /**
     * 拦截指定异常名
     */
    private String throwingName;

    public MyAdvice(Object aspect, Method method) {
        this.aspect = aspect;
        this.adviceMethod = method;
    }

    public Object getAspect() {
        return aspect;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public String getThrowingName() {
        return throwingName;
    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }

}
