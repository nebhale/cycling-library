
package com.nebhale.cyclinglibrary.web;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nebhale.cyclinglibrary.repository.CollectionRepository;
import com.nebhale.cyclinglibrary.repository.TypeRepository;

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

}
