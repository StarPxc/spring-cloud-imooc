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
 * @Description: TODO 前置过滤器
 */
@Component
public class TokenFilter extends ZuulFilter {
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
        //从url参数里获取，也可以从
        String token=request.getParameter("token");
        if(StringUtils.isBlank(token)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());//权限不足
        }
        return null;
    }
}
