package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleComment;
import com.kosta.mbtisland.entity.MbattleVoter;

public interface MbattleService {
	public List<Mbattle> selectMbattleListByPageAndSearchAndSort
	(PageInfo pageInfo, String search, String sort) throws Exception;	// 게시글 목록 조회
	public List<Mbattle> selectDailyHotMbattle() throws Exception; // 일간 인기 게시글 조회
	public Mbattle selectMbattleByNo(Integer no) throws Exception; // Entity 게시글 조회
	public void increaseViewCount(Integer no) throws Exception; // 조회수 증가
	public Integer insertMbattle(Mbattle mbattle) throws Exception; // 게시글 작성
	public void deleteMbattle(Integer no) throws Exception; // 게시글 삭제
	
	public List<MbattleComment> selectMbattleCommentListByMbattleNoAndPage(Integer no, PageInfo pageInfo) throws Exception; // 댓글 목록 조회
	public Integer selectMbattleCommentCountByMbattleNo(Integer no) throws Exception; // 댓글 개수 조회
	public void insertMbattleComment(MbattleComment mbattleComment) throws Exception; // 댓글 작성
	public void deleteMbattleComment(Integer commentNo) throws Exception; // 댓글 삭제
	
	public MbattleVoter selectIsVoteByUsernameAndPostNo(String username, Integer no) throws Exception; // 투표 데이터 조회
	//특정유저로 MbattleList 가져오기
	public List<Mbattle> findByWriterIdAndPage(String username,PageInfo pageInfo) throws Exception;
	//noList로 게시글삭제
	public void deleteMbattleListByNoList(List<Integer> noList) throws Exception;
}
