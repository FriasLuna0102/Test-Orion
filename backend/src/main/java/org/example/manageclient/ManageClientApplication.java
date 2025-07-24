package org.example.manageclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ManageClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageClientApplication.class, args);


    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the Manage Client Application!";
    }



}


