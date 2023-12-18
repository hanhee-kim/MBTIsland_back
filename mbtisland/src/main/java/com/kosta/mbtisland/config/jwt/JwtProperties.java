package com.kosta.mbtisland.config.jwt;

public interface JwtProperties {
	String SECRET = "MBTIsland";	//우리서버 고유 비밀키
	int EXPIRATION_TIME = 60000*60*24; //24시간
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
	

}
