
package com.nebhale.cyclinglibrary.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan
@EnableWebMvc
class WebConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createJsonConverter());
    }

    private HttpMessageConverter<?> createJsonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        converter.setSupportedMediaTypes(Arrays.asList( //
            ApplicationMediaType.TYPE, //
            ApplicationMediaType.COLLECTION, //
            ApplicationMediaType.ITEM, //
            ApplicationMediaType.ITEM_JSON, //
            ApplicationMediaType.TASK));

        return converter;
    }
}
