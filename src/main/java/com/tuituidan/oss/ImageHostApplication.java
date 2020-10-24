package com.tuituidan.oss;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * 本服务的根启动类.
 *
 * @author zhujunhan
 */
@SpringBootApplication
@Slf4j
public class ImageHostApplication implements CommandLineRunner {

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(ImageHostApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String port = environment.getProperty("local.server.port");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        log.info("http://localhost:" + port + contextPath);
    }
}
