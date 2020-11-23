package com.mmc.datareportzuulserver.filter;

import com.mmc.datareportzuulserver.entity.UrlTemplate;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        for (String externalurl : UrlTemplate.EXTERNALURLS) {
            if (externalurl.equals(request.getRequestURI())){
                return false;
            }
        }
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
        System.out.println("====> ok !");
        return null;
    }


    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = (String) stringRedisTemplate.opsForValue().get(token);
            System.out.println( "username:" + username);
            if (username != null) {
                stringRedisTemplate.expire(token, expireTime, TimeUnit.SECONDS);
                setRequestParameter(request, username);
                return true;
            }
        }
        return false;
    }

    public void setRequestParameter(HttpServletRequest request , String username) {
        RequestContext rc = RequestContext.getCurrentContext();
        request.getParameterMap();
        Map<String, List<String>> requestQueryParams = rc.getRequestQueryParams();

        if (requestQueryParams==null) {
            requestQueryParams=new HashMap<>();
        }
        ArrayList<String> usernameList = new ArrayList<>();
        usernameList.add(username);
        requestQueryParams.put("current_username", usernameList);
        rc.setRequestQueryParams(requestQueryParams);
    }
}
