package com.example.Crawling.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class SentimentRestTemplate {
    private static RestTemplate restTemplate = new RestTemplate();

    static{
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    private SentimentRestTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(20*1000);
        factory.setConnectTimeout(20*1000);
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
