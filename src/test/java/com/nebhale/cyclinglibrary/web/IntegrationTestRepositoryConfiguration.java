
package com.nebhale.cyclinglibrary.web;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nebhale.cyclinglibrary.repository.CollectionRepository;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.repository.TaskRepository;
import com.nebhale.cyclinglibrary.repository.TypeRepository;
import com.nebhale.cyclinglibrary.util.PointParser;

@Configuration
class IntegrationTestRepositoryConfiguration {

    @Bean
    TypeRepository typeRepository() {
        return mock(TypeRepository.class);
    }

    @Bean
    CollectionRepository collectionRepository() {
        return mock(CollectionRepository.class);
    }

    @Bean
    ItemRepository itemRepository() {
        return mock(ItemRepository.class);
    }

    @Bean
    TaskRepository taskRepository() {
        return mock(TaskRepository.class);
    }

    @Bean
    PointParser pointParser() {
        return mock(PointParser.class);
    }

}
