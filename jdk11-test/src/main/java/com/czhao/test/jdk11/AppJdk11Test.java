package com.czhao.test.jdk11;

import com.czhao.test.jdk11.aop.ControllerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author zhaochun
 */
@SpringBootApplication
@MapperScan("com.czhao.test.jdk11.db.dao")
public class AppJdk11Test {
    public static void main(String[] args) {
        SpringApplication.run(AppJdk11Test.class, args);
    }

    @Bean
    public ControllerInterceptor controllerInterceptor() {
        return new ControllerInterceptor();
    }
}
