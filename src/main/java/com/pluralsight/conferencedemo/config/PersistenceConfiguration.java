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
    //If the raw values are ever needed, here they are for reference. They need to be added as env variables to work (ex. export DB_USER=postgres):
    //DB_URL=jdbc:postgresql://localhost:5432/conference_app
    //DB_USER=postgres
    //DB_PASSWORD=Welcome
    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${DB_URL}")
    private String dbUrl;

    //Why @Bean - is this so Spring can manage it ?
    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.url(dbUrl);
        //set the username and password IF env variables for them are set, otherwise, leave at the defaults.
        if (dbUsername != null) dsBuilder.username(dbUsername);
        if (dbPassword != null) dsBuilder.password(dbPassword);
        System.out.println("My custom datasource bean has been initialized and set");
        return dsBuilder.build();
    }

}
