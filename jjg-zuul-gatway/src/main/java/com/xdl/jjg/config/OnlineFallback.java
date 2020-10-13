package com.xdl.jjg.config;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OnlineFallback implements ZuulFallbackProvider {

    @Override

    public String getRoute() {
        return "jjg-online";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override

            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override

            public String getStatusText() throws IOException {
                return HttpStatus.OK.toString();
            }

            @Override

            public void close() {

            }

            @Override

            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("服务器内部错误".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return headers;
            }

        };

    }

}
