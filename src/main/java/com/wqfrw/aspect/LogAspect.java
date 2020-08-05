package com.wqfrw.aspect;

/**
 * @ClassName LogAspect
 * @Description TODO(切面类)
 * @Author 我恰芙蓉王
 * @Date 2020年08月05日 10:03
 * @Version 2.0.0
 **/

public class LogAspect {

    /**
     * 功能描述: 前置通知
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 17:24:30
     * @param
     * @return: void
     **/
    public void before(){
        System.err.println("=======前置通知=======");
    }

    /**
     * 功能描述: 后置通知
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 17:24:40
     * @param
     * @return: void
     **/
    public void after(){
        System.err.println("=======后置通知=======\n");
    }

    /**
     * 功能描述: 异常通知
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 17:24:47
     * @param
     * @return: void
     **/
    public void afterThrowing(){
        System.err.println("=======出现异常=======");
    }
}
