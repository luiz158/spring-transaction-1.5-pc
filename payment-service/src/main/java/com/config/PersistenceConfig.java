package com.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by sanjaya on 12/4/16.
 */
@EnableTransactionManagement
@EntityScan(basePackages = {"com.entity"})
@EnableJpaRepositories(basePackages = {"com.repository"})
@Configuration
public class PersistenceConfig {

}
