package com.minis.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.web.context.WebApplicationContext;
import com.minis.web.context.support.AnnotationConfigWebApplicationContext;
import com.minis.web.context.support.XmlScanComponentHelper;
import com.minis.web.method.HandlerMethod;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.HandlerMapping;
import com.minis.web.servlet.ModelAndView;
import com.minis.web.servlet.View;
import com.minis.web.servlet.ViewResolver;

/**
 * Servlet 控制器
 *
 * 作用：
 *   加载配置文件 (minisMVC-servlet.xml) 中的 servlet，并规定servlet拦截的所有 HTTP 请求
 *
 * 注：
 *   Service 类由 Listener 创建 (Listener初始化时会创建 IoC 容器，由IoC容器实例化Service类)
 *   Controller 类由 DispatchServlet 创建
 */
public class DispatcherServlet extends HttpServlet {

    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    // 父级上下文，负责 (在Listener初始化时) 创建 Service 类的实例
    private WebApplicationContext parentApplicationContext;

    // 子级上下文，负责 (在Servlet初始化时) 创建 Controller 类的实例
    private WebApplicationContext webApplicationContext;

    private String contextConfigLocation;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /**
     * 记录需要扫描的包名
     */
    private List<String> packageNames = new ArrayList<>();

    /**
     * 记录所有 controller 的名称
     */
    private List<String> controllerNames = new ArrayList<>();

    /**
     * 记录 controller 名称与 controller 实例的映射关系
     */
    private Map<String, Object> controllerObjs = new HashMap<>();

    /**
     * 记录 controller 名称与 controller 类型的映射关系
     */
    private Map<String, Class<?>> controllerClasses = new HashMap<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().
            getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 获取配置 servlet 的配置文件 (即定义 Controller 的 minisMVC-servlet.xml 文件) 路径
        this.contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        // 创建子级上下文，负责实例化 Controller
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, parentApplicationContext);

        // 加载 bean
        refresh();
    }

    protected void refresh() {
        initController();
        System.out.println(this.controllerNames);

        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
        initViewResolvers(this.webApplicationContext);
    }

    protected void initController() {
        this.controllerNames = Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
        for (String controllerName : this.controllerNames) {
            try {
                this.controllerClasses.put(controllerName, Class.forName(controllerName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                this.controllerObjs.put(controllerName, this.webApplicationContext.getBean(controllerName));
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        try {
            this.handlerMapping = (HandlerMapping) wac.getBean(HANDLER_MAPPING_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
    protected void initHandlerAdapters(WebApplicationContext wac) {
        try {
            this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            return;
        }

        HandlerAdapter handlerAdapter = this.handlerAdapter;
        ModelAndView mav = handlerAdapter.handle(request, response, handlerMethod);

        // 渲染展示页面
        render(request, response, mav);
    }

    /**
     * 渲染返回给前端展示的页面 (视图中填充数据)，写到 HttpServletResponse 中
     */
    protected void render( HttpServletRequest request, HttpServletResponse response, ModelAndView mav) throws Exception {
        // 直接返回 response 中的字符串，不需要渲染页面
        if (mav == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        // 获取视图名
        String sTarget = mav.getViewName();
        // 获取返回数据
        Map<String, Object> modelMap = mav.getModel();
        // 根据视图名找到对应的视图实现
        View view = resolveViewName(sTarget, modelMap, request);

        // 将返回数据渲染到视图中，并把视图写到 HttpServletResponse 中供前端展示
        view.render(modelMap, request, response);
    }

    protected View resolveViewName(String viewName, Map<String, Object> model, HttpServletRequest request) throws Exception {
        if (this.viewResolver != null) {
            View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

}
