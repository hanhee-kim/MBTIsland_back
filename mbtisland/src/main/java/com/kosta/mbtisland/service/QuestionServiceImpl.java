package com.kosta.mbtisland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Question;
import com.kosta.mbtisland.repository.QuestionDslRepository;
import com.kosta.mbtisland.repository.QuestionRepository;

@Service
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private QuestionDslRepository questionDslRepository;
	
	
	/* 관리자페이지 */
	
	// 문의글 목록 (검색, 필터, 페이징)
	@Override
	public List<Question> questionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageInfo pageInfo, String username) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<Question> questionList = questionDslRepository.findQuestionListBySearchAndFilterAndPaging(searchTerm, isAnswered, pageRequest, username);
		
		if(questionList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		Integer allCount = questionCntByCriteria(isAnswered, searchTerm, username);
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if(pageInfo.getCurPage()>allPage) pageInfo.setCurPage(allPage); // 게시글 삭제시 예외처리
		
		return questionList;
	}


	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요) 
	@Override
	public Integer questionCntByCriteria(String isAnswered, String searchTerm, String username) throws Exception {
		// 경우의 수 2^
		Long questionCnt = 0L;
		if (isAnswered == null && searchTerm == null && username == null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, null, null);
		
		else if (isAnswered != null && searchTerm == null && username == null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(isAnswered, null, null);
		else if (isAnswered == null && searchTerm != null && username == null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, searchTerm, null);
		else if (isAnswered == null && searchTerm == null && username != null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, null, username);
		
		else if (isAnswered != null && searchTerm != null && username == null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(isAnswered, searchTerm, null);
		else if (isAnswered != null && searchTerm == null && username != null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(isAnswered, null, username);
		else if (isAnswered == null && searchTerm != null && username != null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, searchTerm, username);
		
		else if (isAnswered != null && searchTerm != null && username != null) questionCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(isAnswered, searchTerm, username);
		
		
		return questionCnt.intValue();
	}

	// 프론트에 표시하기 위한 전체, 처리, 미처리 게시글수 조회(검색여부, 모아보기여부에 따라 달라지도록 함)
	@Override
	public Map<String, Integer> getQuestionCounts(String searchTerm, String username) throws Exception { // String isAnswered 매개변수 삭제?
		Map<String, Integer> counts = new HashMap<>();
		Long totalCnt = 0L;
		Long answeredCnt = 0L;
		Long answeredNotCnt = 0L;
		
		if(searchTerm==null && username==null) { // x x
			totalCnt = questionRepository.count();
			answeredCnt = questionRepository.countByIsAnswered("Y");
			answeredNotCnt = questionRepository.countByIsAnswered("N");
		
		} else if(searchTerm==null && username!=null) { // x o
			totalCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, null, username); // null x o
			answeredCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("Y", null, username); // Y x o
			answeredNotCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("N", null, username); // N x o
		
		} else if(searchTerm!=null && username==null) { // o x
			totalCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, searchTerm, null); // null o x
			answeredCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("Y", searchTerm, null); // Y o x
			answeredNotCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("N", searchTerm, null); // N o x
		
		} else if(searchTerm!=null && username!=null) { // o o
			totalCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId(null, searchTerm, username); // null o o
			answeredCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("Y", searchTerm, username); // Y o o
			answeredNotCnt = questionDslRepository.countByAnsweredPlusSearchPlusWriterId("N", searchTerm, username); // N o o
		}
		
		counts.put("totalCnt", totalCnt.intValue());
		counts.put("answeredCnt", answeredCnt.intValue());
		counts.put("answeredNotCnt", answeredNotCnt.intValue());
		return counts;
	}
		
		
		
	
	
	
	/* 마이페이지 */

}
