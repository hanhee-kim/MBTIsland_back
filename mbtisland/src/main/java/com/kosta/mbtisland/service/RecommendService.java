package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.Recommend;

public interface RecommendService {
	
	// 추천 데이터 조회
	Recommend selectRecommend(String username, Integer postNo, String boardType) throws Exception;

}
