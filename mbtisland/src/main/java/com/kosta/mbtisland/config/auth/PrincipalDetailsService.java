package com.kosta.mbtisland.config.auth;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;


// security 설정에서 loginProcessingUrl("/loginProc");
// /loginProc 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다.
// (AuthenticationManager를 거쳐 AuthenticationProvider에 의해 호출됨)
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	//Security Session(내부 Authentication(내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username);
		System.out.println("loadUserByUsername:"+userEntity);
		if(userEntity!=null) {	//login시 DB에 회원이 있으면?
			userEntity.setVisitCnt(userEntity.getVisitCnt()+1);
			if(userEntity.getBanDate() != null && userEntity.getBanDate().before(new Timestamp(new Date().getTime()))) {
				System.out.println("정지회원 풀어주기 ");
				userEntity.setBanDate(null);
				userEntity.setIsBanned("N");
			}
			userRepository.save(userEntity);
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
