package com.imooc.product.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.dataobject
 * @date 2018/6/14  18:45
 * @Description: TODO
 */
@Data
@Entity
public class ProductCategory {
    @Id
    @GeneratedValue
    private Integer categoryId;

    private String categoryName;
    //类目编号
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;
}
