package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.repository.BookmarkRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService {

	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	// 북마크 데이터 조회
	@Override
	public Bookmark selectBookmark(String username, Integer postNo, String boardType) throws Exception {
		return bookmarkRepository.findByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}

	// 북마크 데이터 삽입
	@Override
	public void insertBookmark(Bookmark bookmark) throws Exception {
		bookmarkRepository.save(bookmark);
	}

	// 북마크 데이터 삭제
	@Override
	public void deleteBookmark(Integer no) throws Exception {
		bookmarkRepository.deleteById(no);
	}

	// 로그인유저의 게시글 북마크여부 조회
	@Override
	public Boolean selectIsBookmarkByUsernameAndPostNoAndBoardType(String username, Integer postNo, String boardType) throws Exception {
		return bookmarkRepository.existsByUsernameAndPostNoAndBoardType(username, postNo, boardType);
	}

}
