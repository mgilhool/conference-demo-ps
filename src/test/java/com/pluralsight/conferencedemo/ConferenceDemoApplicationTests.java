package com.pluralsight.conferencedemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
//Injecting these property values so the configuration in PersistanceConfiguration can succeed, but bad practice since the values are now in the clear
//@TestPropertySource(properties = {"spring.datasource.username=postgres", "spring.datasource.password=Welcome"})
public class ConferenceDemoApplicationTests {


	@Test
	public void contextLoads() {
	}

	//Rather than doing the injection above, Inline a custom configuration class for a dummy datasource rather than the prod Postgres one.  This dummy datasource can be used for testing. This REPLACES PersistenceConfiguration (and possibly all other config properties ? )
	@Configuration
	static class MyTestPersistenceConfiguration {

		@Bean
		DataSource createDataSource(){
			// this just is a stub of a datasource, doesn't really connect to anything and doesn't require a username or password.
			DataSourceBuilder dsBuilder = DataSourceBuilder.create();
			return dsBuilder.build();
		}
	}



}
