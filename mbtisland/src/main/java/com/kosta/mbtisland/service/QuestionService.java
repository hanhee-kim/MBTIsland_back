package com.kosta.mbtisland.service;

import java.util.List;
import java.util.Map;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Question;

public interface QuestionService {
	
	// 1. 문의글 목록 일반
	// 문의글 목록 (검색, 필터, 페이징)
	List<Question> questionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageInfo pageInfo) throws Exception;
	
	// 프론트에 표시하기 위한 전체, 처리, 미처리 게시글수 조회(검색어유무에 따라 달라지도록 함)
	Map<String, Integer> getQuestionCounts(String searchTerm, String isAnswered) throws Exception;
	
	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer questionCntByCriteria(String isAnswered, String searchTerm) throws Exception;
	
	
	// 2. 특정 유저의 문의글 모아보기
	List<Question> questionListByUserAndFilterAndPaging(String userId, String isAnswered, PageInfo pageInfo) throws Exception;
	Map<String, Integer> getQuestionCountsByUser(String userId, String isAnswered) throws Exception;
	Integer questionCntByUser(String isAnswered, String userId) throws Exception;
	
	// 문의글 등록하기
	void questionWrite(Question question)throws Exception;
	

}
