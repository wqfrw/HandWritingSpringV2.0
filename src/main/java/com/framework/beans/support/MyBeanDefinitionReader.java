package com.framework.beans.support;

import com.framework.beans.config.MyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName MyBeanDefinitionReader
 * @Description TODO(解析配置文件的工具类)
 * @Author 我恰芙蓉王
 * @Date 2020年08月03日 19:24
 * @Version 2.0.0
 **/

public class MyBeanDefinitionReader {

    private Properties contextConfig = new Properties();

    List<String> registryBeanClasses = new ArrayList<>();

    public MyBeanDefinitionReader(String... configLocations) {
        //1.加载配置文件
        doLoadConfig(configLocations[0]);

        //2.扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));
    }

    public List<MyBeanDefinition> loadBeanDefinitions() {
        List<MyBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
                //对实现接口的处理
                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private MyBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        return new MyBeanDefinition(factoryBeanName,beanClassName);
    }

    /**
     * 功能描述: 加载配置文件
     *
     * @param contextConfigLocation
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 20:30:55
     * @return: void
     **/
    private void doLoadConfig(String contextConfigLocation) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            //加载配置文件
            contextConfig.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 功能描述: 扫描相关的类
     *
     * @param scanPackage
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月03日 20:30:40
     * @return: void
     **/
    private void doScanner(String scanPackage) {
        //获取根目录  拿到com.wqfry替换成/com/wqfrw
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classFile = new File(url.getFile());
        for (File file : classFile.listFiles()) {
            //如果file是文件夹  则递归调用
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                //如果非class文件 则跳过
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName()).replace(".class", "");
                //类名+包路径放入到类名集合中  方便后续实例化
                registryBeanClasses.add(className);
            }
        }
    }

    /**
     * 首字母转小写
     *
     * @param className
     * @return
     */
    private String toLowerFirstCase(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return this.contextConfig;
    }
}
