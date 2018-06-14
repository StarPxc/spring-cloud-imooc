package com.imooc.product.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.vo
 * @date 2018/6/14  19:14
 * @Description: TODO
 */
@Data
public class ProductVO {
    @JsonProperty("name")
    private String categoryName;//返回给前端的是name
    @JsonProperty("type")
    private Integer categoryType;
    @JsonProperty("foods")
    List<ProductInfoVO> productInfoVOList;

}
