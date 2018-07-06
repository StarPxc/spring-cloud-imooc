package com.imooc.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.message
 * @date 2018/6/19  18:26
 * @Description: TODO
 */
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {
    @StreamListener(StreamClient.INPUT)
    public void process(Object message) {
        log.info("StreamReceiver：{}", message);
    }
}
