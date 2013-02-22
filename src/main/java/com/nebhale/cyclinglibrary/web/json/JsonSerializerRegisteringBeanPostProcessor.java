
package com.nebhale.cyclinglibrary.web.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
final class JsonSerializerRegisteringBeanPostProcessor implements BeanPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;

    @Autowired
    JsonSerializerRegisteringBeanPostProcessor(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.objectMapper = new ObjectMapper();

        for (HttpMessageConverter<?> httpMessageConverter : requestMappingHandlerAdapter.getMessageConverters()) {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) httpMessageConverter).setObjectMapper(this.objectMapper);
                break;
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JsonSerializer) {
            JsonSerializer<?> jsonSerializer = (JsonSerializer<?>) bean;

            if (logger.isDebugEnabled()) {
                logger.debug("Registering {} to handle {}", bean.getClass().getCanonicalName(), jsonSerializer.handledType().getCanonicalName());
            }

            this.objectMapper.registerModule(new SimpleModule(beanName).addSerializer(jsonSerializer));
        }

        return bean;
    }
}
