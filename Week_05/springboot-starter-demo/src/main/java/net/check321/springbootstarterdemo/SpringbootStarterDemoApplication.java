package net.check321.springbootstarterdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringbootStarterDemoApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(SpringbootStarterDemoApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);

	}

}
