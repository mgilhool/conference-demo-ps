package com.pluralsight.conferencedemo.config;

//Example of using java configs rather than .properties etc
//we will configure the datasource this way rather than with properties.

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    //Pulling the username and password from the property file, which points to an env variable
    //This way it is set, but not in the source code or files.
    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    //Why @Bean - is this so Spring can manage it ?
    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.url("jdbc:postgresql://localhost:5432/conference_app").username(dbUsername).password(dbPassword);
        System.out.println("My custom datasource bean has been initialized and set");
        return dsBuilder.build();
    }

}
