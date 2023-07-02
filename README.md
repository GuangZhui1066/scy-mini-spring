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