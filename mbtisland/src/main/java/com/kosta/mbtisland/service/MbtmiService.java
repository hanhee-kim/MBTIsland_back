package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtmi;

public interface MbtmiService {
	
	// 주간인기글 목록
	List<Mbtmi> weeklyHotMbtmiList() throws Exception;
	
	// 최신글 목록
	List<Mbtmi> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo) throws Exception;
	
	// 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer mbtmiCntByCriteria(String category, String type, String searchTerm) throws Exception;
	

}
