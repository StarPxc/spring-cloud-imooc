package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.repository
 * @date 2018/6/14  18:50
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository categoryRepository;
    @Test
    public void findByCategoryTypeIn() {
       List<ProductCategory> categoryList =categoryRepository.findByCategoryTypeIn(Arrays.asList(11,12));
        Assert.assertTrue(categoryList.size()!=0);
    }
}