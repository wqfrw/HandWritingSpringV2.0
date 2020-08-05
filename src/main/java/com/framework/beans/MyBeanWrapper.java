package com.framework.beans;

/**
 * @ClassName MyBeanWrapper
 * @Description TODO(原生对象和代理对象的包装类)
 * @Author 我恰芙蓉王
 * @Date 2020年08月03日 19:25
 * @Version 2.0.0
 **/

public class MyBeanWrapper {

    /**
     * 实例对象
     */
    private Object wrapperInstance;

    /**
     * 对象的类型
     */
    private Class wrapperClass;


    public MyBeanWrapper(Object instance) {
        this.wrapperClass = instance.getClass();
        this.wrapperInstance = instance;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class wrapperClass) {
        this.wrapperClass = wrapperClass;
    }
}
