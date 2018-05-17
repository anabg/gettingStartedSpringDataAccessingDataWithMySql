package com.start.springdatawithmysql;

import com.start.springdatawithmysql.repositories.CustomUserRepository;
import com.start.springdatawithmysql.repositories.CustomUserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;


/**
 * DataConfig
 */
@Configuration
//@EnableJpaRepositories(basePackages = "com.mcafee.ft.data.repository")
@EnableTransactionManagement
public class DataConfig {

    @Bean
    public CustomUserRepository customUserRepository(EntityManager entityManager) {
        return new CustomUserRepositoryImpl(entityManager);
    }

}
