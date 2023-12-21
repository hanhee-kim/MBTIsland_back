package com.kosta.mbtisland.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.BookmarkDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.repository.BookmarkDslRepository;
import com.kosta.mbtisland.repository.BookmarkRepository;
import com.kosta.mbtisland.repository.MbtmiRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService {

	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Autowired
	private BookmarkDslRepository bookmarkDslRepository;
	@Autowired
	private MbtmiRepository mbtmiRepository;
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
	
	
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

	// 북마크리스트 유저로 뽑아오기
	@Override
	public List<BookmarkDto> getBookmarkListByUsername(String username , PageInfo pageInfo) throws Exception {
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
//		Long allCount = (long) bookmarkRepository.findByUsername(username).size();
		Long allCount = bookmarkDslRepository.findCntBookmarkListByuser(username);
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		//필터로 적용한 페이지의 내용이 현재 페이지보다 낮을떄 현재 페이지를 1페이지로 세팅해줌
		if(allPage < pageInfo.getCurPage()) {
			pageInfo.setCurPage(1);
		}
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		System.out.println("allCount : "+allCount);
		
		List<Bookmark> bookmarkList = bookmarkDslRepository.findByUsernameAndPaging(username,pageRequest);
//		List<Bookmark> bookmarkList = bookmarkRepository.findByUsername(username);
		List<BookmarkDto> bookmarkDtoList = new ArrayList<>();
		for(Bookmark book : bookmarkList) {
			String boardType = book.getBoardType();
			String title = "";
			Timestamp date = null;
			Integer cnt = 0;
			if(boardType.toUpperCase().equals("MBTMI")) {
				Optional<Mbtmi> oMbtmi = mbtmiRepository.findById(book.getPostNo());
				title = oMbtmi.get().getTitle();
				date = oMbtmi.get().getWriteDate();
				cnt = oMbtmi.get().getRecommendCnt();			
//			} else if(boardType.toUpperCase().equals("MBTWHY")) {
			} else {	
				Optional<Mbtwhy> oMbtwhy = mbtwhyRepository.findById(book.getPostNo());
				title = oMbtwhy.get().getContent();
				date = oMbtwhy.get().getWriteDate();
				cnt = oMbtwhy.get().getRecommendCnt();
			} 
			bookmarkDtoList.add(BookmarkDto.builder()
					.no(book.getNo())
					.username(book.getUsername())
					.postNo(book.getPostNo())
					.boardType(book.getBoardType())
					.boardTitle(title)
					.writeDate(date)
					.commentCnt(cnt)
					.build()
					);
		}
		return bookmarkDtoList;
	}

	//북마크리스트 삭제
	@Override
	public void deleteBookmarkList(List<Integer> noList) throws Exception {
		for (Integer no : noList) {
			Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(no);
			if (optionalBookmark.isPresent()) {
				bookmarkRepository.deleteById(no);
			} else {
				throw new Exception("해당 번호 북마크 없음");
			}
		}
	}
	

}
