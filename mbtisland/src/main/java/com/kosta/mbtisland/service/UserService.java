package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.UserEntity;

public interface UserService {
	//회원가입
	void joinUser(UserEntity user) throws Exception;
	//idx로 유저 조회
	UserEntity getUserByUserIdx(Integer userIdx) throws Exception;
	//username으로 유저 조회
	UserEntity getUserByUsername(String username) throws Exception;

	

}
