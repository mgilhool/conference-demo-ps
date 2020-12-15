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

    //With the ':" the string to the right is a default if the value is not set in the property files. this should allow the class to initialize if the values are not set
    //but it might not WORK, just initialize. "NOT SET" has no special meaning, just the string I am using to signify it's not set.
    //The error we are trying to avoid looks like:
    //2020-12-15T22:34:57.560735+00:00 app[web.1]: 2020-12-15 22:34:57.560  WARN 4 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaConfiguration': Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'persistenceConfiguration': Injection of autowired dependencies failed; nested exception is java.lang.IllegalArgumentException: Could not resolve placeholder 'DB_PASSWORD' in value "${DB_PASSWORD}"
    @Value("${spring.datasource.username:NOT SET}")
    private String dbUsername;

    @Value("${spring.datasource.password:NOT SET}")
    private String dbPassword;

    //Not setting a default here - it should always be set or we have BIG problems
    @Value("${spring.datasource.url}")
    private String dbUrl;

    //Why @Bean - is this so Spring can manage it ?
    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.url(dbUrl);
        //set the username and password in the Datasource IF the properties are set to the env variables, otherwise, don't set them
        if (!"NOT SET".equals(dbUsername)) dsBuilder.username(dbUsername);
        if (!"NOT SET".equals(dbPassword)) dsBuilder.password(dbPassword);
        System.out.println("My custom datasource bean has been initialized and set");
        return dsBuilder.build();
    }

}
