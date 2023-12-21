package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleVoter;

public interface MbattleService {
	public Mbattle selectMbattleByNo(Integer no) throws Exception; // Entity 게시글 조회
	public void increaseViewCount(Integer no) throws Exception; // 조회수 증가
	public Integer insertMbattle(Mbattle mbattle) throws Exception; // 게시글 작성
	
	public MbattleVoter selectIsVoteByUsernameAndPostNo(String username, Integer no) throws Exception; // 투표 데이터 조회
}
