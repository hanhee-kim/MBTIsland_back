package com.kosta.mbtisland.config.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kosta.mbtisland.config.auth.PrincipalDetails;
import com.kosta.mbtisland.config.jwt.JwtProperties;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		System.out.println("OAuth2LoginSuccessHandler 진입");
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getUserIdx())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		System.out.println(jwtToken);
			
		response.setCharacterEncoding("UTF-8");
		String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/redirect/"+JwtProperties.TOKEN_PREFIX+jwtToken)
				.build().toUriString();
			response.sendRedirect(targetUrl);		
	}
}
