package com.imooc.order.message;

import com.google.gson.Gson;
import com.imooc.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.order.message
 * @date 2018/6/20  14:11
 * @Description: TODO
 */
@Component
@Slf4j
@Transactional
public class ProductInfoReceiver {
    private static final String PRODUCT_STOCK_TEMPLATE="product_stock_%s";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @RabbitListener(queues = "productInfo")
    public void process(String message) {
        Gson gs = new Gson();
        //message=>ProductInfoOutput
        List<ProductInfoOutput> productInfoOutputs= com.imooc.product.utils.JsonUtil.stringToList(message,ProductInfoOutput.class);
        log.info("从队列{}接受到消息：{}", "productInfo", productInfoOutputs);

        for (ProductInfoOutput productInfo:productInfoOutputs) {
            //接收到库存信息后储存到redis
            stringRedisTemplate.opsForValue().set(
                    String.format(PRODUCT_STOCK_TEMPLATE,productInfo.getProductId()),
                    String.valueOf(productInfo.getProductStock()));
        }




    }
}
