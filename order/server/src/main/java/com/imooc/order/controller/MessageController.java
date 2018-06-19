package com.imooc.order.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.controller
 * @date 2018/6/19  15:09
 * @Description: TODO
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @RequestMapping("/send/{message}")
    public String send(@PathVariable String message){
        amqpTemplate.convertAndSend("myQueue",message);
        return message;
    }

    @RequestMapping("/sendOrder/{message}")
    public String sendOrder(@PathVariable String message){
        amqpTemplate.convertAndSend("myOrder","computer",message);
        return message;
    }
}
