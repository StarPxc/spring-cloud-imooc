package com.imooc.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.config
 * @date 2018/6/18  21:12
 * @Description: TODO
 */
@Data
@Component
@ConfigurationProperties("girl")
@RefreshScope
public class GirlConfig {
    private String name;
    private String age;
}
