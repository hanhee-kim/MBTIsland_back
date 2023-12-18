package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.repository.RecommendRepository;

@Service
public class RecommendServiceImpl implements RecommendService {
	
	@Autowired
	private RecommendRepository recommendRepository;

	// 추천 데이터 조회
	@Override
	public Recommend selectRecommend(String username, Integer postNo, String boardType) throws Exception {
		return recommendRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}

	// 추천 데이터 삽입
	@Override
	public void insertRecommend(Recommend recommend) throws Exception {
		recommendRepository.save(recommend);
	}

	// 추천 데이터 삭제
	@Override
	public void deleteRecommend(Integer no) throws Exception {
		recommendRepository.deleteById(no);
	}

	// 로그인유저의 게시글 추천여부 조회
	@Override
	public Boolean selectIsRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return recommendRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}

}
