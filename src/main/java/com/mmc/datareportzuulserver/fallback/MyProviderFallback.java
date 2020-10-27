package com.mmc.datareportzuulserver.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

//注册bean
@Component
public class MyProviderFallback implements FallbackProvider {



    //指定要处理的远程服务
    @Override
    public String getRoute() {
        //必须是小写,即使 远程服务名的字符有大写，这里也必须换成小写，否则报错，无法执行服务降级操作，【与http网址一样，必须小写】
        return "*";
    }

    /**
     * 具体退路操作逻辑
     */
    private ClientHttpResponse fallbackResponse(){
        return new ClientHttpResponse() {
            //返回http状态
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            //返回状态码
            @Override
            public int getRawStatusCode() throws IOException {
                //200是正常
                return 200;
            }
            //返回状态内容
            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            //目前这里不清楚是干什么的
            @Override
            public void close() {

            }
            //返回响应体
            @Override
            public InputStream getBody() throws IOException {
                //以字节流形式返回
                return new ByteArrayInputStream("beng--i can do nothing,崩溃了,11223344".getBytes("UTF-8"));
            }

            //返回响应头
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                //设置响应数据的编码类型
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return httpHeaders;
            }
        };
    }



    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            System.out.println("异常原因：\n"+reason);
//            logger.info("Excption {}",reason);
        }

        return this.fallbackResponse();
    }
}
