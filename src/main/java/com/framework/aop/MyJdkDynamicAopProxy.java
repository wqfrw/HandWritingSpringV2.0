package com.framework.aop;

import com.framework.aop.aspect.MyAdvice;
import com.framework.aop.support.MyAdviceSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @ClassName MyJdkDynamicAopProxy
 * @Description TODO(生成代理类的工具类)
 * @Author 我恰芙蓉王
 * @Date 2020年08月05日 10:21
 * @Version 2.0.0
 **/

public class MyJdkDynamicAopProxy implements InvocationHandler {

    private MyAdviceSupport support;

    public MyJdkDynamicAopProxy(MyAdviceSupport support) {
        this.support = support;
    }

    /**
     * 功能描述: 返回一个代理对象
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 14:17:22
     * @param
     * @return: java.lang.Object
     **/
    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.support.getTargetClass().getInterfaces(), this);
    }

    /**
     * 功能描述: 重写invoke
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 20:29:19
     * @param proxy
     * @param method
     * @param args
     * @return: java.lang.Object
     **/
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Map<String, MyAdvice> advices = support.getAdvices(method, support.getTargetClass());

        Object result = null;
        try {
            //调用前置通知
            invokeAdvice(advices.get("before"));

            //执行原生目标方法
            result = method.invoke(support.getTarget(), args);

            //调用后置通知
            invokeAdvice(advices.get("after"));
        } catch (Exception e) {
            //调用异常通知
            invokeAdvice(advices.get("afterThrowing"));
            throw e;
        }

        return result;
    }

    /**
     * 功能描述: 执行切面方法
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 11:09:32
     * @param advice
     * @return: void
     **/
    private void invokeAdvice(MyAdvice advice) {
        try {
            advice.getAdviceMethod().invoke(advice.getAspect());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
