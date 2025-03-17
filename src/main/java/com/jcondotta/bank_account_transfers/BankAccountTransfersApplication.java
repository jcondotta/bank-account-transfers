package com.jcondotta.bank_account_transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BankAccountTransfersApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountTransfersApplication.class, args);
	}

}
