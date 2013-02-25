
package com.nebhale.cyclinglibrary.util;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nebhale.cyclinglibrary.repository.TaskRepository;

@Configuration
class IntegrationTestUtilConfiguration {

    @Bean
    TaskRepository taskRepository() {
        return mock(TaskRepository.class);
    }

}
