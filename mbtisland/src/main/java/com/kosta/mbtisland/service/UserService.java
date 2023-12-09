package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.UserEntity;

public interface UserService {
	//회원가입
	void joinUser(UserEntity user) throws Exception;
	//idx로 유저 조회
	UserEntity getUserByUserIdx(Integer userIdx) throws Exception;
	//username으로 유저 조회
	UserEntity getUserByUsername(String username) throws Exception;
	//userEmail로 유저 조회(이메일인증을 받을것이므로 유니크한값이여야함->소셜로그인 때문에 널이 아닐수 있기때문에 분리.)
	UserEntity getUserByUserEmailAndProviderNull(String userEmail) throws Exception;
	//업데이트(중복된 IDX를 가진 유저라면)
	void modifyUser(UserEntity user) throws Exception;
	UserEntity setAddUser(UserEntity user,String mbti) throws Exception;
	
	

}
