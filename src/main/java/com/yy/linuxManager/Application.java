package com.yy.linuxManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.yy.linuxManager.interceptor.CheckAdminUserLoginInterceptor;
import com.yy.linuxManager.interceptor.FileDownloadInterceptor;

/**
 * 启动类配置
 * 继承SpringBootServletInitializer配置启动
 * 实现WebMvcConfigurer配置一些web组件，如intercetor之类的
 * @author yy
 *
 */
@SpringBootApplication//相当于@Configuration、@ComponentScan、@EnableAutoConfiguration
public class Application implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CheckAdminUserLoginInterceptor()).addPathPatterns("/administration/**");
		registry.addInterceptor(new FileDownloadInterceptor()).addPathPatterns("/tmp/**");
	}
}