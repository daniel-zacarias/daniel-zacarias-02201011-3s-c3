package br.com.bandtec.Danielac3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DanielAc3Application {

	public static void main(String[] args) {
		SpringApplication.run(DanielAc3Application.class, args);
	}

}
