/*
 * VirtualThreadApplication.java 2024-02-29
 *
 * @author junyoung.park
 * Copyright 2024. PlayD Corp. All rights Reserved.
 */
package com.wywta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class VirtualThreadApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VirtualThreadApplication.class, args);
        // Stop command를 받았을 때 서버를 종료하는 훅 등록
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    @Override
    public void run(String... args) {
        // 애플리케이션 종료 시 실행할 작업들
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
        log.info("VirtualThreadApplication start");
    }

    private void cleanup() {
        // 리소스 해제 및 정리 작업 수행
        log.info("VirtualThreadApplication Cleaning up resources...");
        // 안전하게 애플리케이션 종료
        SpringApplication.exit(applicationContext, () -> 0);
    }
}

@Slf4j
@RestController
class ApplicationController {

    @GetMapping("/stop")
    public void stopApplication() {
        log.info("stop");
        System.exit(0);
    }
}