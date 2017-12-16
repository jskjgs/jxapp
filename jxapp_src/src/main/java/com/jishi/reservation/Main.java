package com.jishi.reservation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by zbs on 2017/7/3.
 */
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(basePackages={"com.jishi.reservation","com.doraemon.base"})
public class Main implements CommandLineRunner {


    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Main.class);
        app.run();
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("服务启动完毕.");

    }
}
