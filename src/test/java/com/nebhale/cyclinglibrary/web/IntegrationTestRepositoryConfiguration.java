/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
