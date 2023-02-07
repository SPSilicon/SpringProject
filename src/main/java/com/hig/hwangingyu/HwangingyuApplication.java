package com.hig.hwangingyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/application.properties")
public class HwangingyuApplication {

	public static void main(String[] args) {
		SpringApplication.run(HwangingyuApplication.class, args);
	}

}
