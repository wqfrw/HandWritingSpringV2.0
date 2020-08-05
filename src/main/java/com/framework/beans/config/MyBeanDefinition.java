package com.framework.beans.config;

/**
 * @ClassName MyBeanDefinition
 * @Description TODO(存储配置信息)
 * @Author 我恰芙蓉王
 * @Date 2020年08月03日 19:25
 * @Version 2.0.0
 **/
public class MyBeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

    public MyBeanDefinition(String factoryBeanName, String beanClassName) {
        this.factoryBeanName = factoryBeanName;
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
