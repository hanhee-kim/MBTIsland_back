package com.kosta.mbtisland.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.repository.MbtwhyCommentRepository;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;

@Service
public class MbtwhyServiceImpl implements MbtwhyService {
	
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
//	private final MbtwhyRepository mbtwhyRepository;
	
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;
	
	@Autowired
	private MbtwhyCommentRepository mbtwhyCommentRepository;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	@Override
	public List<Mbtwhy> selectMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbti, PageInfo pageInfo, String search, String sort) throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5);
		List<Mbtwhy> mbtwhyList = mbtwhyDslRepository.findMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageRequest, search, sort);
		
		// 페이징 계산
		// MbtwhyController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
		Long allCount = mbtwhyDslRepository.findMbtwhyCountByMbtiAndSearch(mbti, search);
		Integer allPage = allCount.intValue() / pageRequest.getPageSize();
		if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
				
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
						
		return mbtwhyList;
	}
	
	// 게시글 개수 조회 (MBTI 타입, 검색 값, 정렬 옵션)
	@Override
	public Long selectMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception {
		return mbtwhyDslRepository.findMbtwhyCountByMbtiAndSearch(mbti, search);
	}
	
	// 게시글 조회 (게시글 번호)
	@Override
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception {
		return mbtwhyRepository.findByNo(no);
	}
	
	// 게시글 작성
	@Override
	public void insertMbtwhy(Mbtwhy mbtwhy) throws Exception {
		mbtwhyRepository.save(mbtwhy);
	}
	
	// 댓글 목록 조회 (게시글 번호)
	@Override
	public List<MbtwhyComment> selectMbtwhyCommentListByMbtwhyNoAndPage(Integer no, PageInfo pageInfo)
			throws Exception {
		// 페이지 번호, 한 페이지에 보여줄 게시글 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<MbtwhyComment> mbtwhyCommentList = mbtwhyDslRepository.findMbtwhyCommentListByMbtwhyNoAndPage(no, pageRequest);
					
		// 페이징 계산
		// MbtwhyCommentController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
		Long allCount = mbtwhyDslRepository.findMbtwhyCommentCountByMbtwhyNo(no);
		Integer allPage = allCount.intValue() / pageRequest.getPageSize();
		if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
							
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
									
		return mbtwhyCommentList;
	}

	// 댓글 개수 조회 (게시글 번호)
	@Override
	public Long selectMbtwhyCommentCountByMbtwhyNo(Integer no) throws Exception {
		return mbtwhyDslRepository.findMbtwhyCommentCountByMbtwhyNo(no);
	}
	
	@Override
	public void insertMbtwhyComment(MbtwhyComment mbtwhyComment) throws Exception {
		mbtwhyCommentRepository.save(mbtwhyComment);
	}
}
