package com.pluralsight.conferencedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//need to extend SpringBootServletinitializer so that web.xml is generated for tomcat and the context is initialized.
public class ConferenceDemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceDemoApplication.class, args);
	}


	//To run it this way, start the docker container with this command here:
	//  docker run --name tomcat -it --rm -p 18888:8080  -v tomcat_vol:/usr/local/tomcat -w "/usr/local/tomcat" tomcat-tutorial
	// that will start up a docker container and link the /Users/mgilhool/Documents/Tutorials/JavaApplicationDevelopmentWithTomcat/tomcat directory in to the container
	// copy the .war file into ./tomcat/webapps
	// wait for it to build and then you can access it at:   http://localhost:18888/conference-demo-0.0.1-SNAPSHOT/api/v1/sessions  (for example)
	// The Dockerfile in that folder has the env variables (like DB_USER) required to set up the DB connection and uses jdk11 so the Spriong app will run.
	// The database itself needs to be running which can be done with:
	// docker start postgres-demo
	//
	//added the override below - but not explicitly needed based on https://mkyong.com/spring-boot/spring-boot-deploy-war-file-to-tomcat/
	//and https://stackoverflow.com/questions/39567434/spring-boot-application-gives-404-when-deployed-to-tomcat-but-works-with-embedde
	//but unsure of what it does.
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ConferenceDemoApplication.class);
	}
}
