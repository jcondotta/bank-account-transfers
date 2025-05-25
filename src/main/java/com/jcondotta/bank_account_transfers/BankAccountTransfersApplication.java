package com.jcondotta.bank_account_transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BankAccountTransfersApplication {

	//-Dspring.profiles.active=dev
	public static void main(String[] args) {
		SpringApplication.run(BankAccountTransfersApplication.class, args);
	}
}
