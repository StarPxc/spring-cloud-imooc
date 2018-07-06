# springcloud-eureka (application service)

### 说明

一张图说明一切

![1528813831726](https://github.com/StarPxc/learn-springcloud/blob/master/img/p1.png)

此模块是eureka的 Eureka Service端也就是所谓的注册中心，为了防止服务端的单点故障，做个注册中心的集群，新增一个注册中心分别把对方作为服务进行注册

> 调用流程

服务消费者先去注册中心获取服务列表，然后根据负载均衡算法找到对应的服务地址调用其中某个服务

### pom.xml

```xml
<!--增加eureka-server的依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```

### 配置文件说明

> eureka.instance.lease-expiration-duration-in-seconds：30 

表示eureka server至上一次收到client的心跳之后，等待下一次心跳的超时时间，在这个时间内若没收到下一次心跳，则将移除该instance。

- ​    默认为90秒
- ​    如果该值太大，则很可能将流量转发过去的时候，该instance已经不存活了。
- ​    如果该值设置太小了，则instance则很可能因为临时的网络抖动而被摘除掉。
- ​    该值至少应该大于 lease-renewal-interval-in-seconds:

> eureka.instance.lease-renewal-interval-in-seconds: 10 表示eureka client发送心跳给server端的频率。

- 如果在leaseExpirationDurationInSeconds后，server端没有收到client的心跳，则将摘除该instance。除此之外，如果该instance实现了HealthCheckCallback，并决定让自己unavailable的话，则该instance也不会接收到流量。

> eureka.client.registry-fetch-interval-seconds: 30 

表示eureka client间隔多久去拉取服务注册信息，默认为30秒

> eureka.server.enable-self-preservation:false 

是否开启自我保护模式，默认为true。

> eureka.server.eviction-interval-timer-in-ms: 3000

eureka server清理无效节点的时间间隔，默认60000毫秒，即60秒

> 改变service-url

```
client:
  service-url:
    defaultZone: http://localhost:8889/eureka/
```

```java
public EurekaClientConfigBean() {
    this.serviceUrl.put("defaultZone", "http://localhost:8761/eureka/");
    this.gZipContent = true;
    this.useDnsForFetchingServiceUrls = false;
    this.registerWithEureka = true;
    this.preferSameZoneEureka = true;
    this.availabilityZones = new HashMap();
    this.filterOnlyUpInstances = true;
    this.fetchRegistry = true;
    this.dollarReplacement = "_-";
    this.escapeCharReplacement = "__";
    this.allowRedirects = false;
    this.onDemandUpdateStatusChange = true;
    this.clientDataAccept = EurekaAccept.full.name();
}
```

eureka的配置文件最终会实例化一个EurekaClientConfigBean，它的构造参数中指定了serviceUrl的默认值，在yml文件中可以通过缩进的方式去配置map

### 启动类

增加@EnableEurekaServer注解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringcloudEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringcloudEurekaApplication.class, args);
    }
}
```