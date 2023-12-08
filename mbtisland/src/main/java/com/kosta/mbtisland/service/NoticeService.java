package com.kosta.mbtisland.service;

import java.util.List;
import java.util.Map;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;

public interface NoticeService {
	
	// 공지사항 일괄 숨김/해제
	void changeIsHided(Integer[] noArr) throws Exception;
	
	// 공지사항 일괄 삭제
	void deleteNotice(Integer[] noArr) throws Exception;
	
	// 게시글수 조회
	Integer noticeCntByCriteria(String filter, String searchTerm) throws Exception;
	
	// 공지사항 상세 조회
	Notice noticeDetail(Integer no) throws Exception;

	// 공지사항 목록 (검색, 필터, 페이징)
	List<Notice> noticeListBySearchAndFilterAndPaging(String sValue, String isHided, PageInfo pageInfo) throws Exception;
	
	// 프론트에 표시하기 위한 게시글수 조회
	Map<String, Integer> getNoticeCounts() throws Exception;
	
}