package com.yy.linuxManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类配置
 * 继承SpringBootServletInitializer配置启动
 * 实现WebMvcConfigurer配置一些web组件，如intercetor之类的
 * @author yy
 *
 */
@SpringBootApplication//相当于@Configuration、@ComponentScan、@EnableAutoConfiguration
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}