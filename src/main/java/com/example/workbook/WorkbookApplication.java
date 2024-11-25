package com.example.workbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.example.workbook")
public class WorkbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkbookApplication.class, args);
    }

}
