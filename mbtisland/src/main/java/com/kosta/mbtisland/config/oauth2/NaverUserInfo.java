package com.kosta.mbtisland.config.oauth2;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
	
	private Map<String,Object> attributes;
	private String joinOrLogin;
	
	public NaverUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return (String)attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getNickname() {
		return (String)attributes.get("nickname");
	}

	@Override
	public String getJoinOrLogin() {
		return joinOrLogin;
	}

	@Override
	public void setJoinOrLogin(String joinOrLogin) {
		this.joinOrLogin = joinOrLogin;
		
	}

	
}
