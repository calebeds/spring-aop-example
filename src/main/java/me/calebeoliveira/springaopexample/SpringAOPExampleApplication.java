package me.calebeoliveira.springaopexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringAOPExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAOPExampleApplication.class, args);
	}

}
