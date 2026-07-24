package com.finovoria.finastrax.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MasterJdbcTemplateConfig {

    @Bean
    @Qualifier("masterJdbcTemplate")
    public JdbcTemplate masterJdbcTemplate(
            @Qualifier("masterDataSource") DataSource masterDataSource) {

        return new JdbcTemplate(masterDataSource);
    }
}