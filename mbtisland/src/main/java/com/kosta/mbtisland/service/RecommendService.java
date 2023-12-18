package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.Recommend;

public interface RecommendService {
	
	// 추천 데이터 조회
	Recommend selectRecommend(String username, Integer postNo, String boardType) throws Exception;
	
	// 추천 데이터 삽입
	void insertRecommend(Recommend recommend) throws Exception;
	
	// 추천 데이터 삭제
	void deleteRecommend(Integer no) throws Exception;
	
	// 로그인유저의 게시글 추천여부 조회
	Boolean selectIsRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception;
	


}
