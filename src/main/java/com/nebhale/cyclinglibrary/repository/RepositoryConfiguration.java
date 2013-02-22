
package com.nebhale.cyclinglibrary.repository;

import javax.sql.DataSource;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
class RepositoryConfiguration {

    @Bean
    DataSource dataSource() {
        return null;
        // DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

        // Flyway flyway = new Flyway();
        // flyway.setDataSource(dataSource);
        // flyway.migrate();

        // return dataSource;
    }
    
    @Bean
    PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
