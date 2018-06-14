package com.imooc.product.service;

import com.imooc.product.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.service
 * @date 2018/6/14  19:04
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
    @Autowired
    private ProductService productService;
    @Test
    public void findUpAll() {
        List<ProductInfo> list=productService.findUpAll();
        Assert.assertTrue(list.size()>0);
    }
}