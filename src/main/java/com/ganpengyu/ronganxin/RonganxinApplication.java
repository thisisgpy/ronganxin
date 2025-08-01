package com.ganpengyu.ronganxin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
@MapperScan(basePackages = {"com.ganpengyu.ronganxin.dao"})
public class RonganxinApplication {

    public static void main(String[] args) {
        SpringApplication.run(RonganxinApplication.class, args);
    }

}
