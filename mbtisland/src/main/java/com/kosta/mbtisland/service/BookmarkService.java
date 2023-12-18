package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.Bookmark;

public interface BookmarkService {
	
	// 북마크 데이터 조회
	Bookmark selectBookmark(String username, Integer postNo, String boardType) throws Exception;
	
	// 북마크 데이터 삽입
	void insertBookmark(Bookmark bookmark) throws Exception;
	
	// 북마크 데이터 삭제
	void deleteBookmark(Integer no) throws Exception;
	
	// 로그인유저의 게시글 북마크여부 조회
	Boolean selectIsBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception;

}
