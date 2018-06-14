# 笔记

### JPA

bug：实体类已经加上了@Entity注解，jpa还是找不到这个类

修复：主键@Id导的是javax.persistence.Id而不是org.springframework.data.annotation.Id

### lambda

```java
List<Integer> categoryTypeList=productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
```

可以从一个对象集合中获取每个对象的某个属性组成的集合

### Arrays.asList()

用与生成集合（一般测试的时候经常用）

### BeanUtils.copyProperties()

将一个对象的属性值拷贝到另一个对象中，封装VO类的时候经常用。

```java
BeanUtils.copyProperties(productInfo,productInfoVO);//把productInfo对象的属性拷贝到productInfoVO中
```

