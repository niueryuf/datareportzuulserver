package com.mmc.datareportzuulserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class DatareportzuulserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatareportzuulserverApplication.class, args);
	}

}
