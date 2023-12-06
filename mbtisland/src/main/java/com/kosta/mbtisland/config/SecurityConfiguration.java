package com.kosta.mbtisland.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.kosta.mbtisland.config.jwt.JwtAuthenticationFilter;
import com.kosta.mbtisland.config.jwt.JwtAuthorizationFilter;
import com.kosta.mbtisland.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private CorsFilter corsFilter;
	@Autowired
	private UserRepository userRepository;
	
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("security config진입");
		http
		.addFilter(corsFilter)  //다른 도메인 접근 허용
		.csrf().disable() //csrf 공격 비활성화
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //session 비활성화

	//Login
	http
		.formLogin().disable() //로그인 폼 사용 비활성화
		.httpBasic().disable() //httpBasic은 header에 username,password를 암호화하지 않은 상태로 주고 받는다. 이를 사용하지 않겠다.
		.addFilter(new JwtAuthenticationFilter(authenticationManager()));  //UsernamePasswordAuthenticationFilter
	
//    //oauth2Login
//    http
//		.oauth2Login()
//        .authorizationEndpoint().baseUri("/oauth2/authorization")  // 소셜 로그인 url
//        .and()
//        .redirectionEndpoint().baseUri("/oauth2/callback/*")  // 소셜 인증 후 redirect url
//        .and()
//        .userInfoEndpoint().userService(principalOauth2UserService)  // 회원 정보 처리
//        .and()
//        .successHandler(oAuth2LoginSuccessHandler);

    http
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)) //BasicAuthenticationFilter
		.authorizeRequests()
		.antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 로그인 & 권한
		.anyRequest().permitAll(); // 나머지는 허용		
	
}	
	
	
	
}
