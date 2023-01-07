package com.dargon.reggie;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class RegginTakeOutApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegginTakeOutApplication.class, args);
		System.out.println("test");
	}

}
