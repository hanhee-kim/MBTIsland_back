package com.kosta.mbtisland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	
	// 1. 문의글 목록 일반
	// 문의글 목록 (검색, 필터, 페이징)
	@Override
	public List<Question> questionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageInfo pageInfo) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<Question> questionList = questionDslRepository.findQuestionListBySearchAndFilterAndPaging(searchTerm, isAnswered, pageRequest);
		
		if(questionList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		Integer allCount = questionCntByCriteria(isAnswered, searchTerm);
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

	// 프론트에 표시하기 위한 전체, 처리, 미처리 게시글수 조회(검색어유무에 따라 달라지도록 함)
	@Override
	public Map<String, Integer> getQuestionCounts(String searchTerm, String isAnswered) throws Exception {
		Map<String, Integer> counts = new HashMap<>();
		Long totalCnt = questionRepository.count();
		Long answeredCnt = questionRepository.countByIsAnswered("Y");
		Long answeredNotCnt = questionRepository.countByIsAnswered("N");
		
		// 검색수행시 새로 조회
		if(searchTerm!=null) {
			totalCnt = questionDslRepository.countBySearchTerm(searchTerm);
			answeredCnt = questionDslRepository.countBySearchTermPlusAnswered(searchTerm);
			answeredNotCnt = questionDslRepository.countBySearchTermPlusAnsweredNot(searchTerm);
		} 
		
		counts.put("totalCnt", totalCnt.intValue());
		counts.put("answeredCnt", answeredCnt.intValue());
		counts.put("answeredNotCnt", answeredNotCnt.intValue());
		return counts;
	}

	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요)
	@Override
	public Integer questionCntByCriteria(String isAnswered, String searchTerm) throws Exception {
		Long totalCnt = 0L;
		Long answeredCnt = 0L;
		Long answeredNotCnt = 0L;
		
		if(isAnswered==null) {
			if(searchTerm==null) totalCnt = questionRepository.count();
			else totalCnt = questionDslRepository.countBySearchTerm(searchTerm);
			return totalCnt.intValue();
			
		} else if(isAnswered.equals("Y")) { 
			if(searchTerm==null) answeredCnt = questionRepository.countByIsAnswered("Y");
			else answeredCnt = questionDslRepository.countBySearchTermPlusAnswered(searchTerm);
			return answeredCnt.intValue();
			
		} else {
			if(searchTerm==null) answeredNotCnt = questionRepository.countByIsAnswered("N");
			else answeredNotCnt = questionDslRepository.countBySearchTermPlusAnsweredNot(searchTerm);
			return answeredNotCnt.intValue();
		}
	}

	
	// 2. 특정 유저의 문의글 모아보기
	@Override
	public List<Question> questionListByUserAndFilterAndPaging(String userId, String isAnswered, PageInfo pageInfo) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<Question> questionList = questionDslRepository.findQuestionListByUserAndFilterAndPaging(userId, isAnswered, pageRequest);
		
		if(questionList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		Integer allCount = questionCntByUser(isAnswered, userId);
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

	@Override
	public Map<String, Integer> getQuestionCountsByUser(String userId, String isAnswered) throws Exception {
		Map<String, Integer> counts = new HashMap<>();
		Long totalCnt = questionDslRepository.countByUser(userId);
		Long answeredCnt = questionDslRepository.countByUserPlusAnswered(userId);
		Long answeredNotCnt = questionDslRepository.countByUserPlusAnsweredNot(userId);
		
		counts.put("totalCnt", totalCnt.intValue());
		counts.put("answeredCnt", answeredCnt.intValue());
		counts.put("answeredNotCnt", answeredNotCnt.intValue());
		return counts;
	}

	@Override
	public Integer questionCntByUser(String isAnswered, String userId) throws Exception {
		Long totalCnt = 0L;
		Long answeredCnt = 0L;
		Long answeredNotCnt = 0L;
		
		if(isAnswered==null) {
			totalCnt = questionDslRepository.countByUser(userId);
			return totalCnt.intValue();
		} else if(isAnswered.equals("Y")) { 
			answeredCnt = questionDslRepository.countByUserPlusAnswered(userId);
			return answeredCnt.intValue();
		} else {
			answeredNotCnt = questionDslRepository.countByUserPlusAnsweredNot(userId);
			return answeredNotCnt.intValue();
		}
	}

	//문의글 등록
	@Override
	public void questionWrite(Question question) throws Exception {
		questionRepository.save(question);
	}

	//문의글 불러오기
	@Override
	public Question questionDetailByNo(Integer no) throws Exception {
		Optional<Question> question = questionRepository.findById(no);
		if(question.isPresent()) {
			return question.get();
		}else {
			throw new Exception("해당 게시글 존재하지 않음.");
		}
	}

	

}
