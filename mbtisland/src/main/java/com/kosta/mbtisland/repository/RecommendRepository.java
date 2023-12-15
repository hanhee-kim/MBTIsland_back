package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer>{
	Boolean existsByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 값 존재 여부 확인 (유저 아이디, 글 번호, 게시판 유형)
	Integer countByPostNoAndBoardType(Integer postNo, String boardType) throws Exception; // 추천 개수 조회 (글 번호, 게시판 유형)
}
