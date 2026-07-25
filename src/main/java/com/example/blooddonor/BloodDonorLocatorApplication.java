package com.example.blooddonor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the starting point of the whole application.
// Running this file starts the server.
@SpringBootApplication
public class BloodDonorLocatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloodDonorLocatorApplication.class, args);
        System.out.println("Blood Donor Locator is running on http://localhost:8080");
    }

}
