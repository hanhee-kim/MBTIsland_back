package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Notice;

public interface MbtmiService {
	
	// 주간인기글 목록
	List<Mbtmi> weeklyHotMbtmiList() throws Exception;
	
	// 최신글 목록
	List<Mbtmi> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo) throws Exception;
	
	// 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer mbtmiCntByCriteria(String category, String type, String searchTerm) throws Exception;
	
	// mbtmi 상세 조회
	Mbtmi mbtmiDetail(Integer no) throws Exception;
	
	// 조회수 증가
	void increaseViewCount(Integer no) throws Exception;
	
	// 댓글 목록
	List<MbtmiComment> mbtmiCommentListByMbtmiNo(Integer mbtmiNo, PageInfo pageInfo) throws Exception;
	
	// 댓글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer mbtmiCommentCnt(Integer mbtmiNo) throws Exception;
	
	// 게시글 삭제
	void deleteMbtmi(Integer no) throws Exception;
	

}
