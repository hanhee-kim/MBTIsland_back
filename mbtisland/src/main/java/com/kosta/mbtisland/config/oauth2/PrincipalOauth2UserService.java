package com.kosta.mbtisland.config.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.config.auth.PrincipalDetails;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	//userRequest는 code를 받아서 accessToken을 응답받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("accessToken:"+ userRequest.getAccessToken().getTokenValue());
//		System.out.println("clientRegistration:"+userRequest.getClientRegistration());
//		System.out.println("oAuth2User:"+super.loadUser(userRequest));
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println(oAuth2User);
		System.out.println(oAuth2User.getAttributes());
		return pricessOAuth2User(userRequest, oAuth2User);
	}
	
	private OAuth2User pricessOAuth2User(OAuth2UserRequest userRequest,OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("네이버와 카카오만 지원합니다");
		}
		
		System.out.println(oAuth2UserInfo);
		System.out.println(oAuth2UserInfo.getProvider());
		System.out.println(oAuth2UserInfo.getProviderId());
		Optional<UserEntity> userOptional = 
				userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		UserEntity user = null;
		if(userOptional.isPresent()) { //이미 가입되어 있으면 update
			user = userOptional.get();
			user.setUserEmail(oAuth2UserInfo.getEmail());
			userRepository.save(user);
		} else {  //가입되어있지 않으면 insert
			user = UserEntity.builder().username(oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId())
				.userEmail(oAuth2UserInfo.getEmail())
				.userRole("ROLE_GUEST")
				.provider(oAuth2UserInfo.getProvider())
				.providerId(oAuth2UserInfo.getProviderId())
//				.password(bCryptPasswordEncoder.encode(oauthPassword))
				.build();
			System.out.println(user);
			userRepository.save(user);
		}
		
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}
