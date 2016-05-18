Spring-Boot
==================

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
重新修改为以下配置后，运行正确
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

## Spring Data JPA

```java
@Entity
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;

    protected User(){}

    public User(String firstName,String lastName){
        this.firstName=fristName;
        this.lastName=lastName;
    }

    @Override
    public String toString(){
        return String.format(
                "User[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }
}
```
+ *id* 作为惟一标识符， `@GeneratedValue(strategy=GenerationType.AUTO)`表明它是一个自增字段
+ *firstName* 与 *lastName* 映射为同名字段

```java
    import java.util.List;

    import org.springframework.data.repository.CrudRepository;

    public interface UserRespository extends CrudRepository<User,Long>{
        List<User> findByLastName(String lastName);
    }
```
`CrudRepository` 已经包含了`save`,`delete`,`findOne()`,`findALl()`等预定义方法，可以根据命名约定来扩展。

## 外部配置
Spring-Boot支持扩展配置，所以可以在不同的环境下运行相同的代码。可以通过 *属性文件*，*YAML*文件和 *环境变量*以及 *命令行参数*来配置。属性值可以通赤`@Value`直接注入组件当中。

Spring-Boot按如下顺序执行：
- 命令行参数
- Properties from SPRING_APPLICATION_JSON (inline JSON embedded in an environment variable or system property)
- JNDI attributes from java:comp/env.
- Java System properties (System.getProperties()).
- 系统环境变量
- A RandomValuePropertySource that only has properties in random.*.
- Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants)
- Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants)
- Application properties outside of your packaged jar (application.properties and YAML variants).
- Application properties packaged inside your jar (application.properties and YAML variants).
- @PropertySource annotations on your @Configuration classes.
- Default properties (specified using SpringApplication.setDefaultProperties)
    
例，如下注入`name`属性
```java
    import org.springframework.stereotype.*;
    import org.springframework.beans.factory.annotation.*;

    @Component
    public class MyBean{
        @Value("${name}")
        private String name;

        //...
    }
```
在类路径(如,在jar里面)建立文件`application.propertites`，给`name`设置一个正确的默认值.也可以在jar文件外部，这样会覆盖内部的。同样同过命令行也可实现(`java -jar app.jar --name="Spring"`)

### 配置随机值
在进行安全类或测试的时候，可以考虑通过`RandomValuePropertySource`来注入随机值。支持整型、长整型、字符串等。
```
my.secret=${random.value}
my.number=${random.int}
my.bignumber=${random.long}
my.number.less.than.ten=${random.int(10)}
my.number.in.range=${random.int[1024,65536]}
```
### 应用属性文件
`SpringApplication`按照以下的顺序来加载`application.propertites`文件，并将它们添加到Spring的环境当中:
1. 当前目录里的`/config`子目录
2. 当前目录
3. `config`包下的类路径
4. 根路径

### 文件中的占位符
`application.propertites`中定义的值在会先在已存在的环境变量中查找，所以你可以引用前面定义的变量。
```
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

### 使用YAML
#### 加载YAML
Spring Framework提供了两种方便的方式来加载YAML文件，`YamlPropertiesFactoryBean`将YAML看作`Properties` ,而 `YamlMapFactoryBean` 将YAML看作是一个Map.
例，下面的YAML文件
```YAML
environments:
    dev:
        url: http://dev.bar.com
        name: Developer Setup
    prod:
        url: http://foo.bar.com
        name: My Cool App
```
将会被转换为下面的属性：
```
environments.dev.url=http://dev.bar.com
environments.dev.name=Developer Setup
environments.prod.url=http://foo.bar.com
environments.prod.name=My Cool App
```

可以通过Spring的`DataBinder`(`@ConfigurationPropertites`)来绑定值。
```java
    @ConfigurationProperties(prefix="my")
    public class Config{
        private List<String> servers=new ArraList<String>();

        public List<String> getServers(){
            return this.servers;
        }
    }
```

## Spring Web MVC   
Spring Web MVC框架是一个富'model view controller'网页框架，可以通过创建`@Controller`或者`@RestController`组件来处理接收到的HTTP请求。通过使用`@RequestMapping`标记来将controller里的方法映射成HTTP。下面是一个典型的使用`@RestContoller`来解析JSON数据例子：
```java
@RestContoller
@RequestMapping(value="/users")
public class MyRestController{

    @RequestMapping(value="/{user}",method=RequestMethod.GET)
    public User getUser(@PathVariable Long user){
        //...
    }
    @RequestMapping(value="/{user}/customers",method=RequestMethod.GET)
    List<Customer> getUserCustomers(@PathVariabel Long user){
        //..
    }
    @RequestMapping(value="/{user}",method=RequestMethod.DELETE)
    public User deleteUser(@PathVariable Long user){
        //...
    }
}
```
Spring MVC是Spring核心框架的一部分，可以参考官方引用文档。

### Spring MVC自动配置

### HttpMessageConverters
Spring MVC使用`HttpMessageConverter`接口转换HTTP请求的响应消息，对象可以自动转换为JSON或者XML，字符串默认以UTF-8编码。
可以通过使用`HttpMessageConverter`类添加或修改：
```java
import org.springframework.boot.autoconfigure.web.HttpMessageConveters;
import org.springframework.boot.context.annotation.*;
import org.springframework.http.conveter.*;

@Configuration
public class MyConfiguration{

    @Bean
    public HttpMessageConverters customConverters(){
        HttpMessageConverter<?> additional=...
        HttpMessageConverter<?> another=...
        return new HttpMessageConverter(additional,another);
    }
}
```
上下文件中的任何`HttpMessageConverter`组件都会添加到Converter列表中。

##静态内容
Spring Boot默认会从`/static` (或者`/public`、`/resources`、`/META-INF/resources`)目录加载静态内容。Spring Boot使用Spring MVC里的`ResourceHttpRequestHandler`来实现该功能，所以用户可自己实现`WebMvcConfigurerAdapter`并且重载`addResourceHandlers`方法。

##错误处理
Spring Boot默认情况下使用`/error`映射来处理错误，它在servelet容器里注册为一个全局的错误页。在电脑客户端，它生成一个JSON格式的信息，包括HTTP状态码和异常信息；在浏览器端它返回一个`Whitelabel`错误视图(可以通过自定义一个视图来解析error)。通过实现`ErrorController`接口可以完全替代它，然后注册一个该类型的bean,或者简单的添加一个`ErrorAttributes`类型的bean。

通过`@ControllerAdvice`可以自定义JSON数据文档:

```java
@ControllerAdvice(basePackageClasses=FooController.class)
public class FooControllerAdvice extends ResponseEntityExceptionHandler{

    @ExceptionHandler(YourException.class)
    @ResponseBody
    ResponseEntity<?> handleControllerExcepion(HttpServletRequest request,Throwable ex){
        HttpStatus status=getStatus(request);
        return new ResponseEntity<>(new CustomErrorType(status.value(),ex.getMessage()),status);
    }

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode=(Integer)request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
```

上面的例子中，如果 `YourException` 被和`FooController`同一个包下面的controller抛出， a json representation of the `CustomerErrorType` POJO 会替代 `ErrorAttributes` 的表达形式

如果你想更多的特定条件错误，嵌套的servlet容器支持一种均匀的 Java DSL 来自定义 the error handling. Assuming that you have a mapping for /400:
```java
@Bean
public EmbeddedServletContainerCustomizer containerCustomizer(){
    return new MyCustomizer();
}

// ...

private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
    }

}
```