package com.imooc.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.message
 * @date 2018/6/19  14:30
 * @Description: TODO 接收mq消息
 */
@Component
@Slf4j
public class MqReceiver {
    //1.@RabbitListener(queues = "myQueue") 不会自动创建队列
    //2.自定创建队列 2.x才能用 @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    //3.自动创建Exchange和Queue绑定(会自动创建队列)
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),
            exchange = @Exchange("myExchange")
    ))
    public void process(String message) {
        log.info("Message：{}", message);
    }

    /**
     * 数码供应商服务接收消息
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("computerOrder"),
            exchange = @Exchange("myOrder"),
            key = "computer"
    ))
    public void processComputer(String message) {
        log.info("computer Message：{}", message);
    }

    /**
     * 水果供应商服务接收消息
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fruitOrder"),
            exchange = @Exchange("myOrder"),
            key = "fruit"
    ))
    public void processFruit(String message) {
        log.info("fruit Message：{}", message);
    }
}
