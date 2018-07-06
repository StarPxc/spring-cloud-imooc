package com.imooc.order.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.message
 * @date 2018/6/19  18:24
 * @Description: TODO
 */
public interface StreamClient {
    String INPUT = "myMessage";
    String OUTPUT = "myMessage";

    @Input(StreamClient.INPUT)
    SubscribableChannel input();

    @Output(StreamClient.OUTPUT)
    MessageChannel output();
}
