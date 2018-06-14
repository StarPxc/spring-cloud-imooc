package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.repository
 * @date 2018/6/14  15:57
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
   @Autowired
    private ProductInfoRepository productInfoRepository;
    @Test
    public void findByProductStatus() {
        List<ProductInfo> list=productInfoRepository.findByProductStatus(0);
        Assert.assertTrue(list.size()>0);
    }
}