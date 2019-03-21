package org.cetc.vqm.config;

import org.cetc.vqm.component.LoginHandlerInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
 * 指明当前类是配置类，替代Spring配置文件
 */
@Configuration
//使用WebMvConfigurerAdapter可以扩展SpringMVC功能
public class AppMvcConfig implements WebMvcConfigurer{
	
	
	/*@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		//浏览器发送 /loginin 请求来到 index页面
		//registry.addViewController("/").setViewName("login");
		//registry.addViewController("/login.html").setViewName("login");
		registry.addViewController("/index.html").setViewName("index");
		//registry.addViewController("/static/systemManage/account.html").setViewName("/systemManage/account.html");
		//registry.addViewController("/static/systemManage/device.html").setViewName("/systemManage/device.html");
		//registry.addViewController("/static/systemManage/log.html").setViewName("/systemManage/log.html");
		
		
	}*/
	
	@Bean
	LoginHandlerInterceptor localInterceptor() {
        return new LoginHandlerInterceptor();
    }
	
	
	//注册拦截器（主要拦截重定向？）
	
	public void addInterceptors(InterceptorRegistry registry) {
		//super.addInterceptors(registry);
		//Spring boot 已经做好了静态资源映射，不需要处理(屏蔽)静态资源
		registry.addInterceptor(localInterceptor()).addPathPatterns("/##")     // new LoginHandlerInterceptor()
		.excludePathPatterns("/login.html","/","/user/login");
//		
//		
	}

	
	
	
}
