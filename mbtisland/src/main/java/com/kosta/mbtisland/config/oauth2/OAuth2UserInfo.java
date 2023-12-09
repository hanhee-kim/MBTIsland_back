package com.kosta.mbtisland.config.oauth2;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getNickname();
}
