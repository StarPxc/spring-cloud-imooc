# Spring Boot微服务（慕课网学习笔记）

## 单体架构和分布式架构对比

单体架构的优点：容易测试，容易部署

单体架构的缺点：

- 开发效率低
- 代码维护难
- 部署不灵活（指的是构建时间特别长，有任何的代码修改都需要重新构建）
- 稳定性不高（牵一发动全身）
- 扩展性不足 

### 简单的微服务架构

![架构图](https://github.com/StarPxc/spring-cloud-imooc/blob/master/img/3.png) 

## 服务注册中心：Eureka

### 注册中心集群配置

通过如下配置相互注册

```yml
eureka1：
client:
  service-url:
    defaultZone: http://localhost:8761/eureka/ #注册到另一个注册中心
 eureka2 ：  
client:
    service-url:
      defaultZone: http://localhost:8762/eureka/ #注册到另一个注册中心
```
### 客户端发现和服务端发现

eureka是客户端发现，客户端从注册中心拿到可用服务列表在进行负载均衡。

nginx,zookeeper,kubernetes是服务端发现，服务端使用代理，使用负载均衡算法选择一个服务暴露给客户端

### 微服务的特点：异构

- 可以使用不同的语言
- 可以使用不同的数据库

### 微服务拆分

#### 起点和重点

- 起点：既有的架构形态
- 终点：好的架构不是设计出来的，而是进化而来的。一直在演进

#### 不适合微服务的业务场景

- 系统中包含很多很强的事物场景
- 业务相对稳定，迭代周期长
- 访问压力不大，可用性要求不高

#### 拆分的方法论

##### 扩展立方模型：

- X轴：水平复制，通过副本扩展，通过负载均衡运行多个完全一样的程序
- Z轴：数据分区，每个服务器负责一个数据子集，每个服务器运行的代码是一样的
- Y轴：功能解耦，将不同职责的模块分成不同的服务。

##### 如何拆分“功能”

###### 单一职责、松耦合、高内聚

- 单一职责：每个服务只负责业务功能的一个单独的部分
- 松耦合：服务之间耦合度低，修改一个服务其他服务不需要修改
- 高内聚：服务内部相关的行为都聚集在一个服务内，要修改一个服务时只需要修改一个服务即可

###### 关注点分离

- 按职责：给我们的服务进行分类，比如订单，商品，app等
- 按通用性：一些基础组件与具体的业务无关的也可以单独拿出来，比如消息服务，用户服务等
- 按粒度级别：订单服务后期可能要拆分成订单服务和支付服务两个服务

###### 服务和数据的关系

- 先考虑业务功能，在考虑数据
- 无状态服务：如果一个数据需要被多个服务共享才能完成一个请求，那么这个数据就可以成为状态，微服务架构中需要将有状态的业务服务改变成的无状态服务，比如session

### SpringCloud中服务调用的两种方式

#### RestTemplate：

##### 第一种方式

```java
@GetMapping("/getProductMsg")
public String getProductMsg(){
    //RestTemplate的第一种方式(直接使用restTemplate，url写死)
    RestTemplate restTemplate=new RestTemplate();
    String response=restTemplate.getForObject("http://localhost:8000/msg",String.class);
    log.info("response={}",response);
    return response;
}
```

缺点：服务地址写死

##### 第二种方式

```java
//第二种方式 （利用loadBalancerClient通过应用名称获取url）
RestTemplate restTemplate = new RestTemplate();
ServiceInstance instance = loadBalancerClient.choose("PRODUCT");
String url = String.format("http://%s:%s", instance.getHost(), instance.getPort() + "/msg");
String response = restTemplate.getForObject(url, String.class);
log.info("response={}", response);
return response;
```

#### 第三种方式

先写一个配置类

```
@Component
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

然后直接可以通过编写应用的名称来调用服务

```
//第三种方式（）
String response = restTemplate.getForObject("http://PRODUCT/msg", String.class);
log.info("response={}", response);
return response;
```

#### feign方式

1. 在pom.xml中添加依赖

   依赖一定要写对

```xml
 <!--错误写法 写错了pom也不会报错，直到启动的时候会报NoClassDefFoundError: feign/Feign$Builder-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-feign</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-boot-starter-feign</artifactId>
</dependency>

 <!--正确写法-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```



2. 在启动类上添加注解@EnableFeignClients

3. 声明调用的方法

   ```
   @FeignClient(name = "product")//访问product应用下面的msg接口
   public interface ProductClient {
       @GetMapping("/msg")
       String productMsg();
   }
   ```

4. 调用接口

```java
@Autowired
private ProductClient productClient;
  @GetMapping("/getProductMsg")
    public String getProductMsg() {
        String response= productClient.productMsg();
        //通过feign
        log.info("response={}", response);
        return response;
    }
```

## 客户端负载均衡：Ribbon



修改负载均衡算法（默认是轮询）

```yml
PRODUCT:# 服务名
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```



