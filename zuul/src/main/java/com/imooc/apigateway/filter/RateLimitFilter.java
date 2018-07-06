package com.imooc.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.imooc.apigateway.exception.RateLimitException;
import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;


/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.apigateway.filter
 * @date 2018/7/6  16:30
 * @Description: TODO 限流拦截器
 */
@Component
public class RateLimitFilter extends ZuulFilter {
    private static final RateLimiter RATE_LIMITER=RateLimiter.create(100);//每秒放100个令牌
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER-1;//优先级要最高
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        if(!RATE_LIMITER.tryAcquire()){//获取令牌
            throw new RateLimitException();
        }
        return null;
    }
}
