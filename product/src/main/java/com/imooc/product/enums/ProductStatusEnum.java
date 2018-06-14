package com.imooc.product.enums;

import lombok.Getter;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.enums
 * @date 2018/6/14  19:00
 * @Description: TODO 商品上下架状态枚举
 */
@Getter
public enum ProductStatusEnum {
    UO(0,"在架"),
    DOWM(1,"下架"),
    ;
    private Integer code;
    private String message;
    ProductStatusEnum(int code, String message) {
        this.code=code;
        this.message=message;
    }
}
