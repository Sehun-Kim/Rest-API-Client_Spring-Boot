package com.example.restClient;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;


@SpringBootApplication
public class RestClientApplication {


	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(RestClientApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);

//		SpringApplication.run(RestClientApplication.class, args);
	}
	
}
