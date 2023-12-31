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
//	List<MbtmiDto> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo, String sort) throws Exception;
	List<MbtmiDto> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo, String sort, String username) throws Exception;
	
	// 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	Integer mbtmiCntByCriteria(String category, String type, String searchTerm, String username) throws Exception;
	
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
	
	// 게시글 등록
	Mbtmi addMbtmi(MbtmiDto mbtmiDto) throws Exception;
	
	// 추천수 증가, 감소
	void increaseRecommendCnt(Integer no) throws Exception;
	void decreaseRecommendCnt(Integer no) throws Exception;
	
	// 댓글의 대댓글 수 조회
	Integer mbtmiChildCommentCnt(Integer mbtmiCommentNo) throws Exception;
	
	// 게시글 수정
	Mbtmi modifyMbtmi(MbtmiDto mbtmiDto) throws Exception;
	
	// 배열번호로 mbtmi게시글 삭제
	void deleteMbtmiList(List<Integer> noList)throws Exception;
	
	// fileIdxs 업데이트
	Mbtmi updateFileIdxs(Integer postNo, Integer fileIdx) throws Exception;
	
	// fileIdxs를 포함하는 content로 업데이트
	Mbtmi updateContainingFileIdxs(MbtmiDto mbtmiDto) throws Exception;
	
	// 댓글 조회
	MbtmiComment mbtmiComment(Integer no) throws Exception;
	
	// 게시글 등록 (업데이트용)
	void addMbtmiForUpdate(Mbtmi mbtmi) throws Exception;
}
