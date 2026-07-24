package com.finovoria.finastrax.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MasterDataSourceConfig {


    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {

        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/finastrax_db")
                .username("root")
                .password("Atharv123")
                .driverClassName(
                    "com.mysql.cj.jdbc.Driver"
                )
                .build();
    }
}