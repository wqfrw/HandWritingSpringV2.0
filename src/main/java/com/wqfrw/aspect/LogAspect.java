package com.wqfrw.aspect;

/**
 * @ClassName LogAspect
 * @Description TODO(切面类)
 * @Author 我恰芙蓉王
 * @Date 2020年08月05日 10:03
 * @Version 2.0.0
 **/

public class LogAspect {

    public void before(){
        System.err.println("=======前置通知=======");
    }

    public void after(){
        System.err.println("=======后置通知=======");
    }

    public void afterThrowing(){
        System.err.println("=======出现异常=======");
    }
}
