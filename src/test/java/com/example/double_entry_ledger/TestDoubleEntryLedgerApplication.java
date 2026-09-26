package com.example.double_entry_ledger;

import org.springframework.boot.SpringApplication;

public class TestDoubleEntryLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.from(DoubleEntryLedgerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
