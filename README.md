# Spring-Boot
spring boot 

## 连接数据库
通过外部配置文件中的 spring.datasource.*.配置DataSource. 例如可以在 ` application.properties`文件中如下配置
spring.datasource.url=jdbc:mysql://localhost/test <br>
spring.datasource.username=dbuser <br>
spring.datasource.password=dbpass <br>
spring.datasource.driver-class-name=com.mysql.jdbc.Driver <br>
