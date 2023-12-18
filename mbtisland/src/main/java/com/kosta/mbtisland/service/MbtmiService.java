package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.MbtmiDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;

public interface MbtmiService {
	
	// 주간인기글 목록
	List<MbtmiDto> weeklyHotMbtmiList() throws Exception;
	
	// 최신글 목록
	List<MbtmiDto> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo, String sort) throws Exception;
	
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

	// 댓글 삭제(IS_REMOVED 컬럼값 업데이트)
	void deleteMbtmiComment(Integer commentNo) throws Exception;
	
	// 댓글 작성
	void addMbtmiComment(MbtmiComment mbtmiComment) throws Exception;
	
	// 게시글 작성
	Mbtmi addMbtmi(MbtmiDto mbtmiDto) throws Exception;
	
	// 추천수 증가, 감소
	void increaseRecommendCnt(Integer no) throws Exception;
	void decreaseRecommendCnt(Integer no) throws Exception;
	

}
