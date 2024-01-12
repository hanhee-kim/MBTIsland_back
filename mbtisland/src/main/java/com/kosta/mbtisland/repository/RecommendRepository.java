package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer>{
	Boolean existsByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType); // 추천 여부 조회
	Recommend findByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType); // 추천 조회 (사용자명, 글 번호, 게시판 타입)
	Integer countByPostNoAndBoardType(Integer postNo, String boardType); // 추천 개수 조회 (글 번호, 게시판 유형)
	void deleteById(Integer no); // 추천 삭제 (아이디 인덱스)
}
