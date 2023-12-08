package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;

public interface MbtwhyService {
	public List<Mbtwhy> selectMbtwhyListByMbtiCategoryAndPageAndSearchTypeAndSearchValueAndSortType
		(String mbtiCategory, PageInfo pageInfo, String searchType, String searchValue, String sortType) throws Exception;	// 게시글 목록 조회
	public Long selectMbtwhyCntByMbtiCategory(String mbtiCategory) throws Exception; // 게시글 개수 조회
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception; // 게시글 조회
	public void insertMbtwhy(Mbtwhy mbtwhy) throws Exception;	// 글 작성
	
}
