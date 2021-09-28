package com.tengu.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.tengu.sync.repository")
public class TenguApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenguApplication.class, args);
    }
}
