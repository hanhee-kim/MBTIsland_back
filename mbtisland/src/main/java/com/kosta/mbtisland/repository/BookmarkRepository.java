package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
	Boolean existsByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 북마크 여부 조회
	Bookmark findByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception; // 북마크 조회 (사용자명, 글 번호, 게시판 타입)
	Integer countByPostNoAndBoardType(Integer postNo, String boardType) throws Exception; // 북마크 개수 조회 (글 번호, 게시판 유형)
	void deleteById(Integer no); // 북마크 삭제 (아이디 인덱스)
}
