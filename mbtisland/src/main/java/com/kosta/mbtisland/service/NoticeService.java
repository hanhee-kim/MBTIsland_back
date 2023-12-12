package com.kosta.mbtisland.service;

import java.util.List;
import java.util.Map;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;

public interface NoticeService {
	
	// 공지사항 목록 (검색, 필터, 페이징)
	List<Notice> noticeListBySearchAndFilterAndPaging(String searchTerm, String isHidden, PageInfo pageInfo) throws Exception;
	
	// 프론트에 표시하기 위한 전체, 표시, 숨김 게시글수 조회(검색어유무에 따라 달라지도록 함)
	Map<String, Integer> getNoticeCounts(String searchTerm, String isHidden) throws Exception;
	
	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer noticeCntByCriteria(String filter, String searchTerm) throws Exception;

	// 공지사항 일괄 숨김/해제
	void changeIsHidden(Integer[] noArr) throws Exception;
	
	// 공지사항 일괄 삭제
	void deleteNotice(Integer[] noArr) throws Exception;
	
	// 공지사항 상세 조회
	Notice noticeDetail(Integer no) throws Exception;
	
	// 조회수 증가
	void increaseViewCount(Integer no) throws Exception;

	
	
}
