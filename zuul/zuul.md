# 服务网关Zuul

> 添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
```

> 修改配置文件

```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/
spring:
  cloud:
    config:  #从配置中心获取配置信息
      discovery:
        enabled: true
        service-id: CONFIG #config应用名称
      profile: dev
  application:
    name: api-gateway
```

> 启动类添加注解

```java
@EnableZuulProxy
```

> 通过zuul访问服务

http://localhost:9000/product/product/list

默认路由规则：第一个product是服务名字后面是原本的路径

> 自定义路由

```
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/
spring:
  cloud:
    config:  #从配置中心获取配置信息
      discovery:
        enabled: true
        service-id: CONFIG #config应用名称
      profile: dev
  application:
    name: api-gateway
zuul:
  routes:
#    myProduct:
#      path: /myProduct/**
#      serviceId: product
    #简洁写法
    product: /myProduct/**
management:
  security:
    enabled: false
```

访问http://localhost:9000/myProduct/product/list得到相同的结果

> 查看所有路由

http://localhost:9000/routes

> 排除某些接口

```yml
zuul:
  routes:
    product: /myProduct/**
  ignored-patterns: /**/product/listForOrder

```

> cookie的传递

```yml
zuul:
  routes:
    myProduct:
      path: /myProduct/**
      serviceId: product
      sensitiveHeaders: #敏感头设置为null 原先cookie是敏感头
```

> 自动更新配置

```java
@Component
public class ZuulConfig {
    @ConfigurationProperties("zuul")
    @RefreshScope
    public ZuulProperties zuulProperties(){
        return new ZuulProperties();
    }
}
```

> Zuul过滤器应用场景

前置过滤器（Pre）：限流，鉴权，参数校验，请求转发。

后置过滤器（Post）：统计，日志

> 权限校验

说明：每个请求在地址栏带一个参数，如果参数为空，校验不通过

前置过滤器

```java
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;


@Component
public class TokenFilter extends ZuulFilter {
    @Override
    public String filterType() {//过滤器类型，前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {//过滤器顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest request=requestContext.getRequest();
        //从url参数里获取，也可以从
        String token=request.getParameter("token");
        if(StringUtils.isBlank(token)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());//权限不足
        }
        return null;
    }
}
```

后置过滤器：

```java
@Component
public class AddResponseHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;//post filter的优先级
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletResponse response=requestContext.getResponse();
        response.setHeader("X-Foo",UUID.randomUUID().toString());
        return null;
    }
}
```

> 限流

令牌桶算法

![令牌桶算法](https://github.com/StarPxc/spring-cloud-imooc/blob/master/img/2.png) 

以一定的速率往桶里面添加令牌，如果放不下了就会丢掉。外部请求过来会从令牌桶里面获得令牌，拿到令牌可以继续往下走，拿不到直接拒绝。

代码：

```java
@Component
public class RateLimitFilter extends ZuulFilter {
    private static final RateLimiter RATE_LIMITER=RateLimiter.create(100);//每秒放100个令牌
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER-1;//优先级要最高
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        if(!RATE_LIMITER.tryAcquire()){//获取令牌
            throw new RateLimitException();
        }
        return null;
    }
}
```

令牌桶算法，谷歌有个开源的组件

```java
com.google.common.util.concurrent.RateLimiter
```

> Zuul权限校验

