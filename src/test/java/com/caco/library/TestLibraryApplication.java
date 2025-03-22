package com.caco.library;

import org.springframework.boot.SpringApplication;

public class TestLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibraryReservationApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
