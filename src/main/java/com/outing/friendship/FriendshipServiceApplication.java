package com.outing.friendship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@SpringBootApplication(scanBasePackages = {"com.outing.auth", "com.outing.friendship","com.outing.commons"}, exclude = {DataSourceAutoConfiguration.class })
@EnableFeignClients(basePackages = "com.outing.auth")
public class FriendshipServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(FriendshipServiceApplication.class, args);
	    System.out.println("Friendship-service-main Started!");
	}




	@Bean
	public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestCustomizer(){
		return (AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry)->{
			registry
					.requestMatchers("/friend-service/**").permitAll()
					.anyRequest().authenticated();
			            
		};
	}


}
