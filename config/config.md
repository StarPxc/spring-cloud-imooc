# 统一配置中心

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

### 启动类添加注解

```java
@EnableConfigServer
```

### 在github上新建仓库并编写配置文件

```yml
spring:
  application:
    name: config
  cloud:
    config:
      server:
        git:
          uri: https://github.com/StarPxc/config-repo
          username: ###
          password: ###
          basedir: E:\Software installation path\configGitRepository #配置本地git仓库存放地址
```

### 尝试访问配置文件

http://localhost:8002/order-test.yml  看看是否能找到配置文件 

http://localhost:8002/order-dev.properties  可以自动转换格式

#### 命名格式

{name}-{profiles}.yml

{label}/{name}-{profiles}.yml

- name 文件名
- profiles 环境
- label 分支 默认是master



------



# 重新配置order服务（从配置中心读取）

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-client</artifactId>
</dependency>
```

### 不需要在启动类上加注解

### 将application.yml变成bootstrap.yml

要确保获取配置中心文件的配置最先被执行所以要使用bootstrap.yml

```yml
spring:
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG #config应用名称
      profile: dev #与github上的order-dev.yml中名称对应dev
  application:
    name: order #与github上的order-dev.yml中名称对应order
```

### 如果不使用eureka默认的端口那么eureka的配置要放在本地的bootstrap.yml上。

```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/
```

### 从配置中心拉取配置

从配置中心拉取配置例如order-test.xml会将order.yml和order-test.yml一起拉去然后合并

# SpringCloud Bus

结合RabbitMQ实现自动更新配置

### 导入pom

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

然后启动后，rabbitMQ就会多出一个队列

### 修改配置文件

#### springboot 1.x

```
management:
  security:
    enabled: false
```

### springboot 2.x

```
management:
  endpoints:
    web:
      expose: "*"
```

### 通知刷新

在git上修改配置文件后，需要发送一条请求来通知刷新

#### springboot 1.x 

  POST http://localhost:8002/bus/refresh 

#### springboot 2.x 

POST http://localhost:/actuator/bus-refresh

### 在需要刷新的地方一定要加上注解@RefreshScope

### 使用配置文件配置信息

### 配置文件定义信息

```
girl:
  name: zjw
  age: 21

```



#### 定义一个配置类

```
@Data
@Component
@ConfigurationProperties("girl") #定义前缀 与配置文件中的名称对应
@RefreshScope
public class GirlConfig {
    private String name;
    private String age;
}
```

### 在controller中使用

```java
@RestController
@RequestMapping("girl")
@RefreshScope
public class GirlController {
    @Autowired
    private GirlConfig girlConfig;
    @GetMapping("/print")
    public String print(){
        return "name："+girlConfig.getName()+"；age："+girlConfig.getAge();
    }
}
```