# RabbitMQ

### MQ应用场景

异步处理 日志处理 流量削峰 应用解耦

### RabbitMq的使用

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

