package com.mmc.datareportzuulserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
public class PreZuulFilter extends ZuulFilter {

    @Value("#{T(java.lang.Integer).parseInt('${token.expire:600}')}")
    private Integer expireTime;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext rc = RequestContext.getCurrentContext();
        return true;
    }

    @Override
    public Object run() {
        RequestContext rc = RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();
        if (!checkToken(request)) {
            String body = "{\"message\":\"用户未登录！\",\"status\":621}";
            rc.getResponse().setContentType("text/html;charset=UTF-8");
            rc.setResponseBody(body);
            rc.setSendZuulResponse(false);
        }
        return null;
    }


    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String key = token ;
            String flyingsessionid = (String)stringRedisTemplate.opsForValue().get(key);
            if (flyingsessionid != null) {
                stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                return true;
            }
        }
        return false;
    }
}
