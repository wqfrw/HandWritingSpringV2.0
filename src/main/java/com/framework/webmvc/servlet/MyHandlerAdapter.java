package com.framework.webmvc.servlet;

import com.framework.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MyHandlerAdapter
 * @Description TODO(请求参数动态赋值)
 * @Author 我恰芙蓉王
 * @Date 2020年08月04日 15:04
 * @Version 2.0.0
 **/

public class MyHandlerAdapter {

    /**
     * 功能描述: 进行参数适配
     *
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月05日 19:41:38
     * @param req
     * @param resp
     * @param mappedHandler
     * @return: com.framework.webmvc.servlet.MyModelAndView
     **/
    public MyModelAndView handle(HttpServletRequest req, HttpServletResponse resp, MyHandlerMapping mappedHandler) throws Exception {

        //保存参数的名称和位置
        Map<String, Integer> paramIndexMapping = new HashMap<>();

        //获取这个方法所有形参的注解   因一个参数可以添加多个注解  所以是一个二维数组
        Annotation[][] pa = mappedHandler.getMethod().getParameterAnnotations();

        /**
         * 获取加了MyRequestParam注解的参数名和位置   放入到paramIndexMapping中
         */
        for (int i = 0; i < pa.length; i++) {
            for (Annotation annotation : pa[i]) {
                if (!(annotation instanceof MyRequestParam)) {
                    continue;
                }
                String paramName = ((MyRequestParam) annotation).value();
                if (!"".equals(paramName.trim())) {
                    paramIndexMapping.put(paramName, i);
                }
            }
        }

        //方法的形参列表
        Class<?>[] parameterTypes = mappedHandler.getMethod().getParameterTypes();

        /**
         * 获取request和response的位置(如果有的话)   放入到paramIndexMapping中
         */
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                paramIndexMapping.put(parameterType.getName(), i);
            }
        }

        //拿到一个请求所有传入的实际实参  因为一个url上可以多个相同的name,所以此Map的结构为一个name对应一个value[]
        //例如：request中的参数t1=1&t1=2&t2=3形成的map结构：
        //key=t1;value[0]=1,value[1]=2
        //key=t2;value[0]=3
        Map<String, String[]> paramsMap = req.getParameterMap();

        //自定义初始实参列表(反射调用Controller方法时使用)
        Object[] paramValues = new Object[parameterTypes.length];

        /**
         * 从paramIndexMapping中取出参数名与位置   动态赋值
         */
        for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
            //拿到请求传入的实参
            String value = entry.getValue()[0];

            //如果包含url参数上的key 则动态转型赋值
            if (paramIndexMapping.containsKey(entry.getKey())) {
                //获取这个实参的位置
                int index = paramIndexMapping.get(entry.getKey());
                //动态转型并赋值
                paramValues[index] = caseStringValue(value, parameterTypes[index]);
            }
        }

        /**
         * request和response单独赋值
         */
        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }
        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }

        //方法调用 拿到返回结果
        Object result = mappedHandler.getMethod().invoke(mappedHandler.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        } else if (mappedHandler.getMethod().getReturnType() == MyModelAndView.class) {
            return (MyModelAndView) result;
        }
        return null;
    }

    /**
     * 功能描述: 动态转型
     *
     * @param value String类型的value
     * @param clazz 实际对象的class
     * @创建人: 我恰芙蓉王
     * @创建时间: 2020年08月04日 16:34:40
     * @return: java.lang.Object  实际对象的实例
     **/
    private Object caseStringValue(String value, Class<?> clazz) throws Exception {
        //通过class对象获取一个入参为String的构造方法  没有此方法则抛出异常
        Constructor constructor = clazz.getConstructor(new Class[]{String.class});
        //通过构造方法new一个实例返回
        return constructor.newInstance(value);
    }
}
