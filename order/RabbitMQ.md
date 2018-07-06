# RabbitMQ

### MQ应用场景

异步处理 日志处理 流量削峰 应用解耦

### 内部结构



![内部结构](https://github.com/StarPxc/spring-cloud-imooc/blob/master/img/1.png)

- Message

消息，消息是不具名的，它由消息头和消息体组成。消息体是不透明的，而消息头则由一系列的可选属性组成，这些属性包括routing-key（路由键）、priority（相对于其他消息的优先权）、delivery-mode（指出该消息可能需要持久性存储）等。

- Publisher

消息的生产者，也是一个向交换器发布消息的客户端应用程序。

- Exchange

  交换器，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。

- Binding

   绑定，用于消息队列和交换器之间的关联。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则，所以可以将交换器理解成一个由绑定构成的路由表。

- Queue

   消息队列，用来保存消息直到发送给消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个队列。消息一直在队列里面，等待消费者连接到这个队列将其取走。

- Connection

   网络连接，比如一个TCP连接。

- Channel

   信道，多路复用连接中的一条独立的双向数据流通道。信道是建立在真实的TCP连接内地虚拟连接，AMQP 命令都是通过信道发出去的，不管是发布消息、订阅队列还是接收消息，这些动作都是通过信道完成。因为对于操作系统来说建立和销毁 TCP 都是非常昂贵的开销，所以引入了信道的概念，以复用一条 TCP 连接。

- Consumer

  消息的消费者，表示一个从消息队列中取得消息的客户端应用程序。

- Virtual Host

   虚拟主机，表示一批交换器、消息队列和相关对象。虚拟主机是共享相同的身份认证和加密环境的独立服务器域。每个 vhost 本质上就是一个 mini 版的 RabbitMQ 服务器，拥有自己的队列、交换器、绑定和权限机制。vhost 是 AMQP 概念的基础，必须在连接时指定，RabbitMQ 默认的 vhost 是 / 。

- Broker

  表示消息队列服务器实体。

 

### RabbitMq的使用

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

注意是springboot不是springcloud

### 定义接收器

```java
@Component
@Slf4j
public class MqReceiver {
    //1.@RabbitListener(queues = "myQueue") 不会自动创建队列
    //2.自定创建队列 2.x才能用 @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    //3.自动创建Exchange和Queue绑定
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue("myQueue"),
            exchange =@Exchange("myExchange")
    ))
    public void process(String message){
        log.info("Message：{}",message);
    }
}
```

### 发送消息

```java
@Autowired
private AmqpTemplate amqpTemplate;
@RequestMapping("/send/{message}")
public String send(@PathVariable String message){
    amqpTemplate.convertAndSend("myQueue",message);
    return message;
}
```

### Exchange的作用

对消息进行分组

```java
/**
 * 数码供应商服务接收消息
 * @param message
 */
@RabbitListener(bindings =@QueueBinding(
        value = @Queue("computerOrder"),
        exchange =@Exchange("myOrder"),
        key = "computer" 
))
public void processComputer(String message){
    log.info("computer Message：{}",message);
}
/**
 * 水果供应商服务接收消息
 * @param message
 */
@RabbitListener(bindings =@QueueBinding(
        value = @Queue("fruitOrder"),
        exchange =@Exchange("myOrder"),
        key = "fruit"
))
public void processFruit(String message){
    log.info("fruit Message：{}",message);
}
```

### 发送消息时指定Exchange

```java
@RequestMapping("/sendOrder/{message}")
public String sendOrder(@PathVariable String message){
    amqpTemplate.convertAndSend("myOrder","computer",message);
    return message;
}
```

RabbitMQ将消息传送到Exchange中，Exchange通过key匹配对应的队列，Exchange执行了路由的功能



------



# Stream

操作消息队列的另一种方法，是对消息中间件的进一步封装，暂时只支持RabbitMQ和Kafka

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```

### 定义接口

```java
public interface StreamClient {
    String INPUT = "myMessage";
    String OUTPUT = "myMessage";

    @Input(StreamClient.INPUT)
    SubscribableChannel input();

    @Output(StreamClient.OUTPUT)
    MessageChannel output();
}
```



### 消息接收者

```java
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {
    @StreamListener(StreamClient.INPUT)
    public void process(Object message){
        log.info("StreamReceiver：{}",message);
    }
}
```

### 消费消息

```java
import org.springframework.messaging.support.MessageBuilder;

@Autowired
private StreamClient streamClient;

@GetMapping("/sendMessageStream/{message}")
public String process(@PathVariable String message){
     streamClient.output().send(MessageBuilder.withPayload(message).build());
    return message;
}
```

***注意MessageBuilder是哪个包下的***

### 配置文件

```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/
spring:
  cloud:
    stream:  #消息分组 保证只有一个实例接收到
      bindings:
        myMessage: #对应StreamClient.INPUT的值
          group: order #应用名字
          content-type: application/json #让Rabbit中存储的对象序列化成json
    config:
      discovery:
        enabled: true
        service-id: CONFIG #config应用名称
      profile: dev
  application:
    name: order #与github上的order-dev.yml中名称对应
```