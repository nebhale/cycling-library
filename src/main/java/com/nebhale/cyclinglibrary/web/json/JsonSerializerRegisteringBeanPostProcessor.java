
package com.nebhale.cyclinglibrary.web.json;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
final class JsonSerializerRegisteringBeanPostProcessor<T> implements BeanPostProcessor {

    private final ObjectMapper objectMapper;

    @Autowired
    JsonSerializerRegisteringBeanPostProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JsonSerializer) {
            JsonSerializer<?> jsonSerializer = (JsonSerializer<?>) bean;
            this.objectMapper.registerModule(new SimpleModule(beanName).addSerializer(jsonSerializer));
        }

        return bean;
    }
}
