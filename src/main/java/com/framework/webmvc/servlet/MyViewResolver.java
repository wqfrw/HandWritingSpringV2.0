package com.framework.webmvc.servlet;

import java.io.File;

/**
 * @ClassName MyViewResolver
 * @Description TODO(模板引擎)
 * @Author 我恰芙蓉王
 * @Date 2020年08月04日 15:29
 * @Version 2.0.0
 **/

public class MyViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    public MyViewResolver(String templateRoot) {
        String filePath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(filePath);
    }

    public MyView resolveViewName(String viewName) {
        if (viewName == null || "".equals(viewName.trim())) {
            return null;
        }
        //格式化页面后缀
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : viewName + DEFAULT_TEMPLATE_SUFFIX;
        //获取页面文件
        File templateFile = new File(templateRootDir.getPath() + File.separator + viewName);
        return new MyView(templateFile);
    }
}
