package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;

public interface MbtwhyService {
	public List<MbtwhyDto> selectMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbtiCategory, PageInfo pageInfo, String search, String sort) throws Exception;	// 게시글 목록 조회
	public Long selectMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception; // 게시글 개수 조회
//	public MbtwhyDto selectMbtwhyDtoByNo(Integer no) throws Exception; // DTO 게시글 조회
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception; // Entity 게시글 조회
	public void increaseViewCount(Integer no) throws Exception; // 조회수 증가
	public MbtwhyDto selectDailyHotMbtwhy(String mbti) throws Exception; // 일간 인기 게시글 조회
	public Integer insertMbtwhy(Mbtwhy mbtwhy) throws Exception; // 게시글 작성
	public void deleteMbtwhy(Integer no) throws Exception; // 게시글 삭제
	
	public List<MbtwhyComment> selectMbtwhyCommentListByMbtwhyNoAndPage(Integer no, PageInfo pageInfo) throws Exception; // 댓글 목록 조회
	public Integer selectMbtwhyCommentCountByMbtwhyNo(Integer no) throws Exception; // 댓글 개수 조회
	public void insertMbtwhyComment(MbtwhyComment mbtwhyComment) throws Exception; // 댓글 작성
	public void deleteMbtwhyComment(Integer commentNo) throws Exception; // 댓글 삭제
	public Integer selectMbtwhyChildCommentCount(Integer commentNo) throws Exception; // 대댓글 개수 조회
		
//	public Recommend selectRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 게시글 추천 데이터 조회
//	public Boolean selectIsRecommendByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 게시글 추천 여부 조회
//	public Integer selectRecommendCountByPostNoAndBoardType(Integer postNo, String boardType) throws Exception; // 게시글 추천수 조회
	public void increaseRecommendCnt(Integer no) throws Exception; // 추천수 증가
	public void decreaseRecommendCnt(Integer no) throws Exception; // 추천수 감소
//	public void insertRecommend(Recommend recommend) throws Exception; // 게시글 추천
//	public void deleteRecommend(Integer no) throws Exception; // 게시글 추천 취소
	
//	public Bookmark selectBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 게시글 북마크 데이터 조회
//	public Boolean selectIsBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 게시글 북마크 여부 조회
//	public void insertBookmark(Bookmark ㅠookmark) throws Exception; // 게시글 북마크
//	public void deleteBookmark(Integer no) throws Exception; // 게시글 북마크 취소
	
	List<Mbtwhy> getMyMbtwhyListByPage(String username,PageInfo pageInfo) throws Exception; //마이페이지에서 내가 작성한 게시글 불러오기
	void deleteMbtwhyList(List<Integer> noList)throws Exception; 
	public MbtwhyComment selectMbtwhyComment(Integer no) throws Exception; // 댓글 조회
}
