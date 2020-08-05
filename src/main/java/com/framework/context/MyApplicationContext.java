package com.framework.context;

import com.framework.annotation.MyAutowired;
import com.framework.annotation.MyController;
import com.framework.annotation.MyService;
import com.framework.aop.MyJdkDynamicAopProxy;
import com.framework.aop.config.MyAopConfig;
import com.framework.aop.support.MyAdviceSupport;
import com.framework.beans.MyBeanWrapper;
import com.framework.beans.config.MyBeanDefinition;
import com.framework.beans.support.MyBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName MyApplicationContext
 * @Description TODO(Spring顶层容器封装)
 * @Author 我恰芙蓉王
 * @Date 2020年08月03日 19:22
 * @Version 2.0.0
 **/

public class MyApplicationContext {

    private String[] configLocations;

    /**
     * 解析配置文件的工具类
     */
    private MyBeanDefinitionReader beanDefinitionReader;

    /**
     * BeanName与className的缓存
     */
    private Map<String, MyBeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * IOC容器
     */
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new HashMap<>();

    /**
     * 原生对象的缓存
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

   /**
    * 功能描述: 初始化MyApplicationContext
    *
    * @创建人: 我恰芙蓉王
    * @创建时间: 2020年08月03日 18:54:01
    * @param configLocations
    * @return:
    **/
    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;

        try {
            //1.读取配置文件并解析BeanDefinition对象
            beanDefinitionReader = new MyBeanDefinitionReader(configLocations);
            List<MyBeanDefinition> beanDefinitionList = beanDefinitionReader.loadBeanDefinitions();

            //2.将解析后的BeanDefinition对象注册到beanDefinitionMap中
            doRegisterBeanDefinition(beanDefinitionList);

            //3.触发创建对象的动作,调用getBean()方法(Spring默认是延时加载)
            doCreateBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 对容器的初始化
     *
     * @param
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 19:38:01
     * @return: void
     **/
    private void doCreateBean() {
        beanDefinitionMap.forEach((k, v) -> getBean(k));
    }

    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitionList) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitionList) {
            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    /**
     * 功能描述: 真正触发IoC和DI的动作  1.创建Bean  2.依赖注入
     *
     * @param beanName
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 19:48:58
     * @return: java.lang.Object
     **/
    public Object getBean(String beanName) {
        //============ 创建实例 ============

        //1.获取配置信息,只要拿到beanDefinition对象即可
        MyBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        //用反射创建实例  这个实例有可能是代理对象 也有可能是原生对象   封装成BeanWrapper统一处理
        Object instance = instantiateBean(beanName, beanDefinition);
        MyBeanWrapper beanWrapper = new MyBeanWrapper(instance);

        factoryBeanInstanceCache.put(beanName, beanWrapper);

        //============ 依赖注入 ============
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrapperInstance();
    }

    private MyAdviceSupport instantiateAopConfig(MyBeanDefinition beanDefinition) {
        MyAopConfig myAopConfig = new MyAopConfig();

        myAopConfig.setPointCut(beanDefinitionReader.getConfig().getProperty("pointCut"));
        myAopConfig.setAspectClass(beanDefinitionReader.getConfig().getProperty("aspectClass"));
        myAopConfig.setAspectBefore(beanDefinitionReader.getConfig().getProperty("aspectBefore"));
        myAopConfig.setAspectAfter(beanDefinitionReader.getConfig().getProperty("aspectAfter"));
        myAopConfig.setAspectAfterThrowing(beanDefinitionReader.getConfig().getProperty("aspectAfterThrowing"));
        myAopConfig.setAspectAfterThrowingName(beanDefinitionReader.getConfig().getProperty("aspectAfterThrowingName"));

        return new MyAdviceSupport(myAopConfig);

    }

    /**
     * 功能描述: 依赖注入
     *
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 20:09:01
     * @return: void
     **/
    private void populateBean(String beanName, MyBeanDefinition beanDefinition, MyBeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperClass();

        //只有加了注解的类才需要依赖注入
        if (!(clazz.isAnnotationPresent(MyController.class) || clazz.isAnnotationPresent(MyService.class))) {
            return;
        }

        //拿到bean所有的字段 包括private、public、protected、default
        for (Field field : clazz.getDeclaredFields()) {

            //如果没加MyAutowired注解的属性则直接跳过
            if (!field.isAnnotationPresent(MyAutowired.class)) {
                continue;
            }

            MyAutowired annotation = field.getAnnotation(MyAutowired.class);
            String autowiredBeanName = annotation.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            //强制访问
            field.setAccessible(true);
            try {
                if (factoryBeanInstanceCache.get(autowiredBeanName) == null) { continue; }
                //赋值
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 功能描述: 反射实例化对象
     *
     * @param beanName
     * @param beanDefinition
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 20:08:50
     * @return: java.lang.Object
     **/
    private Object instantiateBean(String beanName, MyBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();

        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();

            /**
             *  ===========接入AOP begin===========
             */
            MyAdviceSupport support = instantiateAopConfig(beanDefinition);
            support.setTargetClass(clazz);
            support.setTarget(instance);
            //如果需要代理  则用代理对象覆盖目标对象
            if (support.pointCutMatch()) {
                instance = new MyJdkDynamicAopProxy(support).getProxy();
            }
            /**
             * ===========接入AOP end===========
             */

            factoryBeanObjectCache.put(beanName, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }

    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return beanDefinitionReader.getConfig();
    }
}
