package com.example.kitchensinkboot.app;

import org.springframework.boot.SpringApplication;

public class TestKitchenSinkBootApplication {

    public static void main(String[] args) {
        SpringApplication.from(KitchenSinkBootApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
