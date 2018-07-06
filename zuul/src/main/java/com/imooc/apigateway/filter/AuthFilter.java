package com.imooc.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.apigateway.filter
 * @date 2018/7/6  16:08
 * @Description: TODO 权限校验（区分买家和卖家）
 */
@Component
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {//过滤器类型，前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {//过滤器顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest request=requestContext.getRequest();
        /**
         * /order/create 只能买家
         * /order/finish 只能卖家访问
         * /product/list 都可以
         */
        
        return null;
    }
}
