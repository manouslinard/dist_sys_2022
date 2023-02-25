package gr.hua.dit.dissys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DissysApplication {

	public static void main(String[] args) {
		SpringApplication.run(DissysApplication.class, args);
	}

}
