package com.kosta.mbtisland.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kosta.mbtisland.entity.UserEntity;

import lombok.Data;

//security가 /loginProc 주소를 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 security session을 만들어준다. (Security ContextHolder)
//security session에 들어가는 타입은 Authentication 타입의 객체여야 한다.
//Authentication안에 User 정보를 넣어야 한다.
//그 User 오브젝트 타입은 UserDetails 타입이어야 한다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
	private UserEntity user;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(UserEntity user) {
		this.user=user;
	}

	public PrincipalDetails(UserEntity user, Map<String, Object> attributes) {
		this.user=user;
		this.attributes=attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getUserRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getUserPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return user.getUserIdx()+"";
	}
}
