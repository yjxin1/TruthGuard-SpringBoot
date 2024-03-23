package com.yjx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableScheduling //开启对定时任务的支持
@ServletComponentScan //开启了对servlet组件的支持
@SpringBootApplication
public class TruthGuardProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruthGuardProjectApplication.class, args);
    }

}
