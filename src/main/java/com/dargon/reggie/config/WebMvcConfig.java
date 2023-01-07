package com.dargon.reggie.config;


import com.dargon.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    /*
    * 静态页面的映射
    * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    log.info("静态页面映射");
       registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
       registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /*
    *扩展mvc框架的消息转换器
    *
    * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        //消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
       //设置对象转换器，底层使用jakson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器添加到mvc框架集合中
        converters.add(0,messageConverter);
    }
}
