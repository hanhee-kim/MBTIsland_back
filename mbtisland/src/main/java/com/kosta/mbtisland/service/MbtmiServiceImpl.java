package com.kosta.mbtisland.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.repository.MbtmiDslRepository;
import com.kosta.mbtisland.repository.MbtmiRepository;

@Service
public class MbtmiServiceImpl implements MbtmiService {
	
	@Autowired
	private MbtmiRepository mbtmiRepository;
	@Autowired
	private MbtmiDslRepository mbtmiDslRepository;

	
	// 주간인기글 목록
	@Override
	public List<Mbtmi> weeklyHotMbtmiList() throws Exception {
		List<Mbtmi> weeklyHotMbtmiList = mbtmiDslRepository.findWeeklyHotMbtmiListByCategoryAndRecommendCnt();
		if(weeklyHotMbtmiList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		return weeklyHotMbtmiList;
	}

	// 최신글 목록
	@Override
	public List<Mbtmi> mbtmiListByCategoryAndTypeAndSearch(String category, String type, String searchTerm, PageInfo pageInfo) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<Mbtmi> mbtmiList = mbtmiDslRepository.findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(category, type, searchTerm, pageRequest);
		
		if(mbtmiList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		Integer allCount = mbtmiCntByCriteria(category, type, searchTerm);
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if(pageInfo.getCurPage()>allPage) pageInfo.setCurPage(allPage); // 게시글 삭제시 예외처리
		
		return mbtmiList;
	}

	// 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	@Override
	public Integer mbtmiCntByCriteria(String category, String type, String searchTerm) throws Exception {
		Long mbtmiCnt = mbtmiRepository.count();
		
		if(category!=null) {
			mbtmiCnt = mbtmiRepository.countByCategory(category); // 카테고리o 타입x 검색어x
			if(type!=null) {
				mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbti(category, type); // 카테고리o 타입o 검색어x
				if(searchTerm!=null) {
					mbtmiCnt = mbtmiDslRepository.countByCategoryPlusWriterMbtiPlusSearch(category, type, searchTerm); // 카테고리o 타입o 검색어o
				}
			}
		}
		
		return mbtmiCnt.intValue();
	}

}
