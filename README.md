# Spring-Boot
spring boot 

## 连接数据库
通过外部配置文件中的 spring.datasource.*.配置DataSource. 例如可以在 ` application.properties`文件中如下配置
spring.datasource.url=jdbc:mysql://localhost/test <br>
spring.datasource.username=dbuser <br>
spring.datasource.password=dbpass <br>
spring.datasource.driver-class-name=com.mysql.jdbc.Driver <br>
### 配置pom
第一次配置的时候，编译成功，运行时总是提示Bean示定义`nested exception is org.springframework.beans.factory.BeanCreationException: Could not autowire field:`。原先添加的依赖为

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
        </dependency>
```
重新修改为以下配置后，运行正砍
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
```

## 日志
    
Spring Boot 是通过调用[Commons Logging](http://commons.apache.org/proper/commons-logging/)来实现内部日志功能。用户可根据自身需要来实现底层的接口。默认的配置为`Java Util Logging, Log4J,Log4J2`和`Logback`
如果选择`Starter POMS`，系统会默认选择Logback

### 日志格式

默认的日志输出格式像下面这样：
``` log
2014-03-05 10:57:51.112  INFO 45469 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/7.0.52
2014-03-05 10:57:51.253  INFO 45469 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2014-03-05 10:57:51.253  INFO 45469 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1358 ms
2014-03-05 10:57:51.698  INFO 45469 --- [ost-startStop-1] o.s.b.c.e.ServletRegistrationBean        : Mapping servlet: 'dispatcherServlet' to [/]
2014-03-05 10:57:51.702  INFO 45469 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean  : Mapping filter: 'hiddenHttpMethodFilter' to:
```