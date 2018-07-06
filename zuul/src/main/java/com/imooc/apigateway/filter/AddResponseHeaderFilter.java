package com.imooc.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.apigateway.filter
 * @date 2018/7/6  16:20
 * @Description: TODO 后置过滤器
 */
@Component
public class AddResponseHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;//post filter的优先级
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletResponse response=requestContext.getResponse();
        response.setHeader("X-Foo",UUID.randomUUID().toString());
        return null;
    }
}
