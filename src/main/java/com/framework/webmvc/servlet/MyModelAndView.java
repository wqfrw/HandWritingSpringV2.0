package com.framework.webmvc.servlet;

import java.util.Map;

/**
 * @ClassName MyModelAndView
 * @Description TODO(视图解析器)
 * @Author 我恰芙蓉王
 * @Date 2020年08月04日 15:05
 * @Version 2.0.0
 **/

public class MyModelAndView {

    /**
     * 返回页面文件名
     */
    private String viewName;

    /**
     * 返回数据
     */
    private Map<String, ?> model;


    public MyModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public MyModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

}
