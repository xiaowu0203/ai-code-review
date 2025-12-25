package com.ai.codereview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * AI代码审核系统主启动类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("com.ai.codereview.mapper")
public class AiCodeReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeReviewApplication.class, args);
    }
}