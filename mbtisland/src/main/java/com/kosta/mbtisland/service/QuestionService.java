package com.kosta.mbtisland.service;

import java.util.List;
import java.util.Map;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Question;

public interface QuestionService {
	
	/* 관리자페이지 */
	// 문의글 목록 (검색, 필터, 페이징)
	List<Question> questionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageInfo pageInfo, String username) throws Exception; 
	
	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer questionCntByCriteria(String isAnswered, String searchTerm, String username) throws Exception;
	
	// 프론트에 표시하기 위한 전체, 처리, 미처리 게시글수 조회(검색어유무에 따라 달라지도록 함)
	Map<String, Integer> getQuestionCounts(String searchTerm, String username) throws Exception;

	
	
	/* 마이페이지 */
	
	// 문의글 등록하기
	void questionWrite(Question question)throws Exception;
	//문의글 번호로 가져오기
	Question questionDetailByNo(Integer no) throws Exception;

}
