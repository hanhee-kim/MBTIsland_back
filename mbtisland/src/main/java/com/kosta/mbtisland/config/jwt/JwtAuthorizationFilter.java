package com.kosta.mbtisland.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kosta.mbtisland.config.auth.PrincipalDetails;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;

//인가 : 로그인 처리가 되야만 하는 요청이 들어왔을때 실행된다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("header Authrization:"+header);
		
		if(header==null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token=request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
		System.out.println("token??: "+token);
		//토큰 검증
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("username").asString();
		System.out.println("username ?? : "+username);
		if(username!=null) {
			System.out.println("username이 널이 아니면?");
			UserEntity user = userRepository.findByUsername(username);
			
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, 
					null, principalDetails.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
	}
}
