package com.kosta.mbtisland.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;

@Service
public class MbtwhyServiceImpl implements MbtwhyService {
	
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
//	private final MbtwhyRepository mbtwhyRepository;
	
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지)
	@Override
	public List<Mbtwhy> selectMbtwhyListByMbtiCategoryAndPageAndSearchTypeAndSearchValueAndSortType
		(String mbtiCategory, PageInfo pageInfo, String searchType, String searchValue, String sortType) throws Exception {
		// 한 페이지에 보여주어야 할 페이지 수
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<Mbtwhy> mbtwhyList = mbtwhyDslRepository.findMbtwhyListByMbtiCategoryAndPageAndSearchTypeAndSearchValueAndSortType(mbtiCategory, pageRequest, searchType, searchValue, sortType);
		
		// 페이징 계산
		// MbtwhyController에서 넘겨준 pageInfo를 참조하기에, 반환하지 않아도 됨
		Long allCount = mbtwhyDslRepository.findMbtwhyCntByMbtiCategory(mbtiCategory);
		Integer allPage = allCount.intValue() / pageRequest.getPageSize();
		if(allCount % pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
				
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
						
		return mbtwhyList;
	}
	
	// 게시글 개수 조회 (MBTI 타입)
	@Override
	public Long selectMbtwhyCntByMbtiCategory(String mbtiCategory) throws Exception {
		return mbtwhyDslRepository.findMbtwhyCntByMbtiCategory(mbtiCategory);
	}
	
	// 게시글 개수 조회 (게시글 번호)
	@Override
	public Mbtwhy selectMbtwhyByNo(Integer no) throws Exception {
		return mbtwhyDslRepository.findMbtwhyByNo(no);
	}
	
	// Mbtwhy 게시글 작성(삽입)
	@Override
	public void insertMbtwhy(Mbtwhy mbtwhy) throws Exception {
		mbtwhyRepository.save(mbtwhy);
	}
}
