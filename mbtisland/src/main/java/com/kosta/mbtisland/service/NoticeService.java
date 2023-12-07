package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;

public interface NoticeService {
	
	// 공지사항 일괄 숨김/해제
	void changeIsHided(Integer[] noArr) throws Exception;
	
	// 공지사항 일괄 삭제
	void deleteNotice(Integer[] noArr) throws Exception;
	
	// 게시글수 조회
	Integer noticeCntByCriteria(String filter) throws Exception;
	
	// 공지사항 상세 조회
	Notice noticeDetail(Integer no) throws Exception;

	// 공지사항 목록 (검색, 필터, 페이징)
	List<Notice> noticeListBySearchAndFilterAndPaging(String sValue, String isHided, PageInfo pageInfo) throws Exception;
}
