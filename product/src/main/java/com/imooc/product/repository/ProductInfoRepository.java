package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.repository
 * @date 2018/6/14  15:53
 * @Description: TODO
 */

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
