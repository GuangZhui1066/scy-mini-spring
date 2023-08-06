# IoC

### IoC 容器继承关系
![img_1.png](IoC容器继承关系.png)

### IoC 容器实现过程
![img_2.png](IoC容器实现过程.png)


# MVC

### MVC 处理流程
![img_3.png](MVC处理流程.png)

### 配置 Tomcat
为 module 添加 web，配置web资源目录 (web资源目录下需要有 web.xml 文件，配置后web资源目录会有小蓝点)
![img.png](images/img1.png)
添加 artifact —— Web Application: exploded </br>
(注：artifacts 是 maven 中的一个概念，表示项目或modules如何打包，比如jar,war,war exploded,ear等打包形式。一个项目或module有了 artifacts 就可以部署到web应用服务器上了)
![img.png](images/img2.png)
![img.png](images/img3.png)
配置 tomcat Configuration，配置其 Deployment，添加 Artifact
![img.png](images/img4.png)
![img.png](images/img5.png)
启动 tomcat，访问 /helloworld
![img.png](images/img6.png)

### Servlet 服务器启动过程
web.xml 文件是 Java 的 Servlet 规范中规定的，它里面声明了一个 Web 应用全部的配置信息。</br>
每个 Java Web 应用都必须包含一个 web.xml 文件，且必须放在 WEB-INF 路径下。</br>
web.xml 文件的顶层根是 web-app，指定命名空间和 schema 规定。 </br>
web.xml 通常包含 context-param、Listener、Filter、Servlet 等元素。</br>

    <servlet></servlet>
    声明servlet类。    

    <servlet-mapping></servlet-mapping>
    声明servlet的访问路径，试一个方便访问的URL。  

    <display-name></display-name>  
    声明WEB应用的名字    

    <description></description>   
    声明WEB应用的描述信息    

    <context-param></context-param>
    声明应用全局的初始化参数。  

    <listener></listener>
    声明监听器，它在建立、修改和删除会话或servlet环境时得到事件通知。

    <filter></filter>
    声明一个实现javax.servlet.Filter接口的类。    

    <filter-mapping></filter-mapping>
    声明过滤器的拦截路径。

    <session-config></session-config>
    session有关的配置，超时值。

    <error-page></error-page>
    在返回特定HTTP状态代码时，或者特定类型的异常被抛出时，能够制定将要显示的页面。   

当 Servlet 服务器 (如 Tomcat) 启动时：
1. 读取 web.xml 文件中的 <context-param> 元素，获取web应用的全局参数，创建 ServletContext 上下文
2. 读取 <context-param> 元素，将其中的参数以键值对形式存入 ServletContext 中
3. 读取 < listener > 元素，创建其定义的监听器实例
4. 读取 < filter > 元素，创建过滤器实例
5. 读取 < servlet > 元素，创建 Servlet 实例 (根据参数 load-on-startup 的大小为优先级)

### MVC 整合 IoC
在创建 listener 的过程中，可以手动插入创建 IoC 容器的逻辑 (在 javax.servlet.ServletContextListener.contextInitialized 方法中)</br>
这样在 SpringMVC 中，就可以访问到 IoC 容器

### 拆分两级上下文
之前 IoC 容器是在 Listener 初始化时创建的，创建 IoC 容器时会实例化出 IoC 容器中管理的那些 Service 的 bean。</br>
Controller 类的实例化则是在 Servlet 初始化时 (com.minis.web.DispatcherServlet.init) 进行的。</br>
现在我们把 Controller 的实例化也交给 MVC 容器 (适用于Web的IoC容器) 去管理，将 MVC 容器拆分为上下两级：
1. XmlWebApplicationContext: 父级，启动在前。负责 IoC 容器的功能，用于创建原来 IoC 容器管理的 Service 类的实例
2. AnnotationConfigWebApplicationContext: 子级，启动在后。负责创建 Controller 类的实例。并且子级持有对父级的引用，可以访问到父级

### Dispatcher 设计模式
通过 HandlerMapping，将请求映射到处理器 </br>
通过 HandlerAdapter，支持多种类型的处理器进行处理 </br>
通过 ViewResolver 解析逻辑视图名到具体视图实现 </br>