package com.imooc.order.controller;

import com.imooc.order.config.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.controller
 * @date 2018/6/18  21:13
 * @Description: TODO
 */
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
