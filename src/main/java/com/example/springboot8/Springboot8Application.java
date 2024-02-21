package com.example.springboot8;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springboot8.mapper")
public class Springboot8Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot8Application.class, args);
	}

}
