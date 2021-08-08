package com.ljm.spring.formework.view;

import java.io.File;
import java.util.Locale;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 17:54
 * @description 把静态文件转换为动态文件
 **/
public class MyViewResolver {
    private final String DEFALUT_TEMPALTE_SUFIX = ".html";

    // 视图目录
    private File templateRootDir;

    public MyViewResolver(String templateRootDir) {
        this.templateRootDir = new File(templateRootDir);
    }

    // 通过页面Name，返回相应View视图
    public View resolveViewName(String viewName, Locale locale) {
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        // 给没有 .html的加上后缀（我们可以在ModelAndView中写500.html，也可以直接写 500）
        viewName = viewName.endsWith(DEFALUT_TEMPALTE_SUFIX) ? viewName : (viewName + DEFALUT_TEMPALTE_SUFIX);
        // 返回相应视图
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new View(templateFile);
    }
}
