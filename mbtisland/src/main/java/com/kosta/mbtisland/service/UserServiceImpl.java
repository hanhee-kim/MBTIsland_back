package com.kosta.mbtisland.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void joinUser(UserEntity user) throws Exception {
		UserEntity userCheck = userRepository.findByUsername(user.getUsername());
		if(userCheck == null) {
			userRepository.save(user);
		}else {
			throw new Exception("아이디 중복");
		}
	}

	@Override
	public UserEntity getUserByUserIdx(Integer userIdx) throws Exception {
		Optional<UserEntity> ouser = userRepository.findById(userIdx);
		if(ouser.isPresent()) {
			return ouser.get();
		}else {
			throw new Exception("유저번호 없음");
		}
	}

	@Override
	public UserEntity getUserByUsername(String username) throws Exception {
		UserEntity user = userRepository.findByUsername(username);
		if(user != null) {
			return user;
		}else {
			return null;
//			throw new Exception("username 없음");
		}
	}

}
