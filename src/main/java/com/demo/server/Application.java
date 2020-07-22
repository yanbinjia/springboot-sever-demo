package com.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@tk.mybatis.spring.annotation.MapperScan(basePackages = {"com.demo.server.dao"})
/**
 * @ServletComponentScan
 * enable scanning for @WebFilter, @WebListener and @WebServlet.
 * 默认情况下SpringBoot,不启用@WebFilter, @WebListener and @WebServlet.
 *
 * @Configuration 与 @ServletComponentScan,同时开启会导致 @WebFilter 加载两次(2.2.8),
 * 所以关闭@ServletComponentScan,使用@Configuration单独开启.
 */
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

}
