package com.imooc.product.dataobject;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.dataobject
 * @date 2018/6/14  15:44
 * @Description: TODO
 */
@Data
@Entity
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStoke;
    private String productDescription;
    private String productIcon;
    private Integer productStatus;
    //类目编号
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;


}
