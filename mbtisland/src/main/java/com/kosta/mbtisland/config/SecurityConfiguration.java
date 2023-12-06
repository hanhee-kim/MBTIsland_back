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
		http
		.addFilter(corsFilter) // 도메인이 다른 곳에서 들어오는것을 허용하는 필터를 넣어줌
		.csrf().disable()	 //다른 도메인으로 부터의 csrf 공격 비활성화
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 비활성화
		.and()
		.formLogin().disable()	//로그인 폼 사용 비활성화
		.httpBasic().disable()	//httpBasic은 header에 username과 password 를 암호화하지 않고 주고 받는다. 이를 사용하지 않겠다.
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))	// DB확인
		.addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
		.authorizeRequests()
//		.antMatchers("/user/**").authenticated() // 로그인해야해!
//		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 로그인 & 권한
//		.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER')")
		.antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
		.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 로그인 & 권한
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		//user 접근은 유저or매니저or관리자 가능
		//manager 접근은 매니저 or 관리자 가능
		//admin 접근은 관리자만 가능
		.anyRequest().permitAll(); // 나머지는 허용
	}	
	//세션에 넣어놓는건 필터에서 저장한 세션을 컨트롤러에서 가져다 쓰라고 만드는 것.
	
	
	
	
	
	
	
	
	
}
