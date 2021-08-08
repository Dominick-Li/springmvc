package com.ljm.spring.formework.servlet;

import com.ljm.spring.formework.annotation.MyRestController;
import com.ljm.spring.formework.annotation.MyController;
import com.ljm.spring.formework.annotation.MyRequestMapping;
import com.ljm.spring.formework.context.MyApplicationContext;
import com.ljm.spring.formework.handler.MyHandlerAdapter;
import com.ljm.spring.formework.handler.MyHandlerMapping;
import com.ljm.spring.formework.view.ModelAndView;
import com.ljm.spring.formework.view.MyViewResolver;
import com.ljm.spring.formework.view.View;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:15
 * @description webb应用初始化类和应用流程处理类
 **/
public class MyDispatcherServlet extends HttpServlet {

    private Logger log = Logger.getLogger(this.getClass().getName());

    MyApplicationContext applicationContext;

    /**
     * 保存接口和方法路由映射关系
     */
    private Map<String, MyHandlerMapping> handlerMapping = new HashMap();

    /**
     * 存放路由和处理器映射关系
     */
    private Map<MyHandlerMapping, MyHandlerAdapter> handlerAdapterMap = new HashMap<>();

    /**
     * 保存视图解析器的容器
     */
    private List<MyViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        log.info("初始化MyDispatcherServlet");
        //初始化应用程序上下文,处理ioc和dl核心功能流程
        applicationContext = new MyApplicationContext(config.getInitParameter("contextConfigLocation"));
        //初始化策略,加载必须要使用的组件
        initStrategies(applicationContext);
    }

    protected void initStrategies(MyApplicationContext applicationContext) {
        // 1.初始化视图转换器，必须实现
        initViewResolvers(applicationContext);
        //2.加载接口和方法映射
        initHandlerMapping(applicationContext);
        //3.加载适配器
        initHandlerAdapter(applicationContext);
    }

    private void initHandlerAdapter(MyApplicationContext applicationContext) {
        MyHandlerAdapter myHandlerAdapter;
        for (MyHandlerMapping myHandlerMapping : handlerMapping.values()) {
            //此处实际用的是同一个适配器,实际可能根据HandlerMapping类型分配不同的适配器
            myHandlerAdapter=new MyHandlerAdapter();
            if(myHandlerAdapter.supports(myHandlerMapping)){
                this.handlerAdapterMap.put(myHandlerMapping,myHandlerAdapter);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("执行MyDispatcherServlet的doPost()");
        try {
            //处理请求
            doDispatch(req, resp);
        } catch (Exception e) {
            try {
                Map model = new HashMap();
                model.put("msg", e.getMessage());
                model.put("stackTrace", e.getStackTrace());
                processDispatchResult(req, resp, new ModelAndView("500.html", model));
            } catch (Exception er) {
                er.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("执行MyDispatcherServlet的doGet()");
        doPost(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handlerMapping.isEmpty()) {
            return;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        if (!this.handlerMapping.containsKey(url)) {
            processDispatchResult(req, resp, new ModelAndView("404.html"));
            return;
        }
        MyHandlerMapping handler = this.handlerMapping.get(url);
        MyHandlerAdapter myHandlerAdapter = getHandlerAdapter(handler);

        ModelAndView myModelAndView = myHandlerAdapter.handle(req, resp, handler);
        //输出结果
        processDispatchResult(req, resp, myModelAndView);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
        if (null == mv) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (MyViewResolver myViewResolver : this.viewResolvers) {
            View view = myViewResolver.resolveViewName(mv.getViewName(), null);
            if (view != null) {
                view.render(mv.getModel(), req, resp);
                return;
            }
        }
    }


    private MyHandlerAdapter getHandlerAdapter(MyHandlerMapping handlerMapping) {
        if (this.handlerAdapterMap.isEmpty()) return null;
        MyHandlerAdapter myHandlerAdapter = this.handlerAdapterMap.get(handlerMapping);
        if (myHandlerAdapter.supports(handlerMapping)) {
            return myHandlerAdapter;
        }
        return null;
    }

    /**
     * 初始化控制器
     */
    private void initHandlerMapping(MyApplicationContext context) {
        if (context.getFactoryBeanObjectCache().size() == 0) {
            return;
        }
        try {
            for (Map.Entry<String, Object> entry : context.getFactoryBeanObjectCache().entrySet()) {
                Class<? extends Object> clazz = entry.getValue().getClass();
                if (!clazz.isAnnotationPresent(MyController.class) && !clazz.isAnnotationPresent(MyRestController.class)) {
                    continue;
                }
                //拼url时,是controller头的url拼上方法上的url
                String baseUrl = "";
                MyRequestMapping annotation;
                if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                    annotation = clazz.getAnnotation(MyRequestMapping.class);
                    baseUrl = annotation.value();
                }

                if (!baseUrl.startsWith("/")) {
                    baseUrl = "/" + baseUrl;
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                        continue;
                    }
                    annotation = method.getAnnotation(MyRequestMapping.class);
                    String url = annotation.value();
                    if (url.trim().length() > 0) {
                        url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    } else {
                        url = baseUrl;
                    }
                    handlerMapping.put(url, new MyHandlerMapping(entry.getValue(), method));
                    System.out.println("mapping " + url + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewResolvers(MyApplicationContext context) {
        // 拿到在配置文件中配置的模板存放路径(layouts)
        String templateRoot = context.getConfig().getProperty("templateRoot");

        // 通过相对路径找到目标后，获取到绝对路径
        // 注：getResourse返回的是URL对象，getFile返回文件的绝对路径
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        // 视图解析器可以有多种，且不同的模板需要不同的Resolver去解析成不同的View（jsp，html，json。。）
        // 但这里其实就只有一种（解析成html）
        this.viewResolvers.add(new MyViewResolver(templateRootPath));
    }


}
