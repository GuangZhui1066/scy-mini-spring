# IoC
### IoC 容器继承关系
![img.png](IoC容器继承关系.png)

### IoC 容器实现过程
![img.png](IoC容器实现过程.png)

### Bean 生命周期
![img.png](bean生命周期.png)



# MVC
### MVC 处理流程
![img.png](MVC处理流程.png)


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
Controller 类的实例化则是在 Servlet 初始化时 (com.minis.web.servlet.DispatcherServlet.init) 进行的。</br>
现在我们把 Controller 的实例化也交给 MVC 容器 (适用于Web的IoC容器) 去管理，将 MVC 容器拆分为上下两级：
1. XmlWebApplicationContext: 父级，启动在前。负责 IoC 容器的功能，用于创建原来 IoC 容器管理的 Service 类的实例
2. AnnotationConfigWebApplicationContext: 子级，启动在后。负责创建 Controller 类的实例。并且子级持有对父级的引用，可以访问到父级


### Dispatcher 设计模式
通过 HandlerMapping，将请求映射到处理器 </br>
通过 HandlerAdapter，支持多种类型的处理器进行处理 </br>
通过 ViewResolver 解析逻辑视图名到具体视图实现 </br>


### 自动转换请求中的参数类型
处理HTTP请求时，只能用 ServletRequest.getParameter() 方法获取参数，返回值是字符串。</br>
因此希望 MVC 框架能自动解析HTTP请求中字符串格式的参数，把字符串转换成需要的类型，比如结构体。</br>


### 将web请求的处理结果返回给前端
调用目标方法得到返回值之后，有两种方式可以把处理结果返回给前端
1. 只返回数据。主流方式，这种方式可以实现前后端分离，后端只是把数据返回给前端，由前端自行渲染界面效果。
2. 返回一个页面。由后端 controller 根据某种规则拿到一个页面，把数据整合进去，然后整个回传给前端浏览器。典型的技术就是 JSP。

处理返回数据与处理参数相反，需要把后端的返回值 (Java对象) 按照某种字符串格式传给前端。



# JDBC
在 Java 体系中，数据访问的规范是 JDBC，也就是 Java Database Connectivity. </br>
JDBC 流程：

    1. 加载数据库驱动程序。
       JDBC 只是提供了一个访问的 API，具体访问数据库的实现是由不同厂商提供的数据库 Driver 实现的 (桥接模式)。对同一种数据库，可以有不同的 Driver。
    2. 获取数据库连接 (Connection 对象)
       建立和断开数据库连接的过程很耗时，所以需要利用数据库连接池技术来提高性能。
    3. 通过 Connection 对象创建 Statement 对象，Statement 对象是对一条 SQL 命令的包装
    4. 使用 Statement 执行 SQL 语句，获取返回的结果集 ResultSet
    5. 操作 ResultSet 结果集，转化为业务对象，执行后续的业务逻辑
    6. 回收数据库资源，关闭数据库连接，释放资源

代码：

    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=DEMO;user=testuser;password=test;");
    stmt = con.createStatement(sql); 
    rs = stmt.executeQuery();
    User user = null;
    if (rs.next()) {
        user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
    }
    rs.close();
    stmt.close();
    con.cloase();


### 连接 MySQL
homebrew 安装 MySQL
启动 mysql

    mysql.server start

navicat 连接 mysql，创建数据库、表


### 抽取 JdbcTemplate
把 JDBC 的标准流程作为模版固定下来，流程中可以变化的部分让子类重写。</br>
注意：需要在 pom 中引入 mysql-connector-java 包，并设置项目的 artifacts，使得编译后的 out 目录中包含 mysql-connector-java 的 jar 包。</br>
![img.png](images/img7.png)

四次重构，重构数据访问和处理的流程
1. 模版化，把通用代码写到 JdbcTemplate 中，把变化的部分交给子类实现
2. 通过 Callback 模式，把上一步中子类里实现的逻辑包装成回调函数，把回调函数当参数传给 JdbcTemplate。这样就不需要为每一个数据表都增加一个具体的实现类
3. 结合 IoC 容器，把 JdbcTemplate 等声明为 bean 管理，并用 @Autowire 注解自动注入
4. 抽取出数据源的概念，管理 Connection，把业务逻辑和底层的数据库管理拆分开

问题：
1. 每次执行sql语句都会建立和关闭一次连接，性能差
2. sql执行的结果是单行数据，不支持查询结果是多行数据
3. PreparedStatement 中参数的赋值没有做到自动化


### SQL 语句配置化
之前的 sql 语句都是在代码中硬编码的，但我们需要把 sql 语句放在代码之外的配置文件里，类似 MyBatis. </br>
MyBatis 是一个支持自定义 SQL、存储过程和复杂映射的框架。它将手工的 JDBC 代码都简化掉了，通过 XML 文件或注解完成数据库记录与 Java 对象的转换。</br>


### 连接池并发问题解决思路

    // 用两个队列分别表示空闲连接和忙碌连接
    private BlockingQueue<PooledConnection> busyConnections; 
    private BlockingQueue<PooledConnection> idleConnections;

    // 获取连接
    while (true) {
        conn = idle.poll();
    }

    // 考虑：获取连接时，如果没有空闲连接，就判断忙碌连接数没有达到最大，没有就创建一个新的连接
    // 因为是多线程，所以这里使用 CAS 技术
    if (size.get() < getPoolProperties().getMaxActive()) {
        if (size.addAndGet(1) > getPoolProperties().getMaxActive()) {
            size.decrementAndGet();
        } else {
            return createConnection(now, con, username, password);
        }
    }

    // 考虑 timeout
    if ((System.currentTimeMillis() - now) >= maxWait) {
        throw new PoolExhaustedException(
            "Timeout: Unable to fetch a connection in " + (maxWait / 1000) + " seconds.");
        } else {
            continue;
        }
    }




# AOP
AOP (Aspect Orient Programming), 面向切面编程. </br>
#### 应用场景
一个类的方法中，除了业务逻辑，还会包括其他的例行性代码 (比如日志功能、权限检查、事务处理，性能监控等等) </br>
为了避免这些例行性代码重复出现，并且影响业务逻辑代码的可读性，可以将这些代码抽取出来，然后在程序运行时动态插入到业务逻辑中。</br>
通过 Java 中的动态代理即可实现。


#### 非侵入性编程
动态代理对代码的侵入性强。需要在业务逻辑中手动地创建代理对象：

    DynamicProxyHelper proxyHelperOne = new DynamicProxyHelper(actionOne);
    ActionOne proxyOne = (ActionOne) proxyHelperOne.getProxy();
    String resultOne = proxyOne.doActionOne(msg);

应该实现非侵入性编程。不用在业务逻辑中手动创建代理，而是直接调用原本的业务接口，把实现类和代理类配置在外面。这样就可以在业务代码中不感知代理对象：

    // 业务代码中直接调用原本的业务接口，不需要感知代理类
    String resultOne = actionOne.doActionOne(msg);

    // 实现类和代理类配置在外面
    <!-- 实现类，即真实对象 -->
    <bean name="actionOneImpl" class="com.test.aop.service.ActionOneImpl" />
    <!-- 代理类，其中包含真实对象 -->
    <bean name="actionOne" class="代理类" >
        <property type="java.lang.Object" name="target" ref="actionOneImpl"/>
    </bean>


### AOP 中的概念
1. Join Point：连接点，表示增强逻辑可以插入的地方
2. Advice：通知，表示在连接点采取的增强操作
3. Advisor：通知者，其中持有一个 Advice 对象，负责管理 Advice
4. Interceptor：拦截器，作用是拦截方法的执行，方便插入增强逻辑
5. Pointcut：切点，定义了 Advice 执行的范围


### 自动为 bean 创建动态代理
目前如果想要为某个对象配置增强操作，就需要写一段如下代码，扩展性很差

    <bean name="actionOne" class="com.minis.aop.framework.ProxyFactoryBean" >
        <property type="java.lang.Object" name="target" ref="actionOneImpl"/>
        <property type="String" name="interceptorName" value="advisor"/>
    </bean>
    <bean name="actionOneImpl" class="com.test.aop.service.ActionOneImpl" />

因此我们需要做到：指定一个规则，并且为符合此规则的所有对象都自动创建代理对象 (ProxyFactoryBean) </br>
在创建 bean 的过程中，会首先创建毛坯 bean 实例，然后为这个 bean 填充属性，然后为这个 bean 执行 BeanPostProcessor 处理器以及 init 方法 </br>
所以我们可以实现一个 BeanPostProcessor 处理器 —— BeanNameAutoProxyCreator ，专门为 bean 创建动态代理类 </br>


### AOP 总结
Spring AOP 是 Spring 框架的一个核心组件之一。面向对象和面向切面，两者一纵一横。 </br>
在 AOP 编程中，Aspect 指的是横切逻辑，就是和基本业务逻辑无关，但却是很多不同业务代码共同需要的功能，比如日志记录、安全检查、事务管理，等等。 </br>
AOP 强调的是将横切逻辑作为一个独立的属性进行处理，而不是直接嵌入到基本业务逻辑中。这样可以提高代码的可复用性、可维护性和可扩展性。 </br>
AOP 基于 JDK 动态代理的实现，虽然很容易实现，但是效率不高，限制也比较多。

