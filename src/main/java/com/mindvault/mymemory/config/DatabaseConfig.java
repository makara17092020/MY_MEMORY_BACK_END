package com.mindvault.mymemory.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (StringUtils.hasText(databaseUrl)) {
            try {
                URI uri = new URI(databaseUrl);
                String username = uri.getUserInfo().split(":")[0];
                String password = uri.getUserInfo().split(":")[1];
                String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath() + "?sslmode=require";
                dataSourceProperties.setUrl(jdbcUrl);
                dataSourceProperties.setUsername(username);
                dataSourceProperties.setPassword(password);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL format", e);
            }
        }
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}