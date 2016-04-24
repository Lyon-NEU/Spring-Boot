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