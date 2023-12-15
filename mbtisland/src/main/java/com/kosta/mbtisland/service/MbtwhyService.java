package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;

public interface MbtwhyService {
	public List<Mbtwhy> selectMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbtiCategory, PageInfo pageInfo, String search, String sort) throws Exception;	// 게시글 목록 조회
	public Long selectMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception; // 게시글 개수 조회
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception; // 게시글 조회
	public void insertMbtwhy(Mbtwhy mbtwhy) throws Exception;	// 게시글 작성
	
	public List<MbtwhyComment> selectMbtwhyCommentListByMbtwhyNoAndPage(Integer no, PageInfo pageInfo) throws Exception; // 댓그 목록 조회
	public Long selectMbtwhyCommentCountByMbtwhyNo(Integer no) throws Exception; // 댓글 개수 조회
	public void insertMbtwhyComment(MbtwhyComment mbtwhyComment) throws Exception; // 댓글 작성
	//마이페이지에서 내가 작성한 게시글 불러오기
	List<Mbtwhy> getMyMbtwhyListByPage(String username,PageInfo pageInfo) throws Exception;
	//List로 isRemoved변경하기
	void updateIsRemoved(List<Integer> noList)throws Exception;
		
}
