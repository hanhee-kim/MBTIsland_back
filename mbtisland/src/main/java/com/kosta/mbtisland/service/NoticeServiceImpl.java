package com.kosta.mbtisland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;
import com.kosta.mbtisland.repository.NoticeDslRepository;
import com.kosta.mbtisland.repository.NoticeRepository;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private NoticeDslRepository noticeDslRepository;

	// 공지사항 일괄 숨김/해제
	@Override
	public void changeIsHided(Integer[] noArr) throws Exception {		
		for (Integer no : noArr) {
			Notice notice = noticeRepository.findById(no).get();
			if(notice==null) throw new Exception("게시글이 존재하지 않습니다.");
			notice.setIsHided(notice.getIsHided().equals("N")? "Y": "N");
			noticeRepository.save(notice);
		}
	}

	// 공지사항 일괄 삭제
	@Override
	public void deleteNotice(Integer[] noArr) throws Exception {
		for (Integer no : noArr) {
			Notice notice = noticeRepository.findById(no).get();
			if(notice==null) throw new Exception("게시글이 존재하지 않습니다.");
			noticeRepository.deleteById(no);
		}
	}
	
	

	// PageInfo계산시 필요한 게시글수 조회 (필터, 검색어 적용)
	@Override
	public Integer noticeCntByCriteria(String filter, String searchTerm) throws Exception {
		Long totalCnt = 0L;
		Long displayCnt = 0L;
		Long hiddenCnt = 0L;
		
		if(filter==null) {
			if(searchTerm==null) totalCnt = noticeRepository.count();
			else totalCnt = noticeDslRepository.countBySearchTerm(searchTerm);
			return totalCnt.intValue();
			
		} else if(filter.equals("N")) { 
			if(searchTerm==null) displayCnt = noticeRepository.countByIsHided("N");
			else displayCnt = noticeDslRepository.countBySearchTermPlusDisplay(searchTerm);
			return displayCnt.intValue();
			
		} else {
			if(searchTerm==null) hiddenCnt = noticeRepository.countByIsHided("Y");
			else hiddenCnt = noticeDslRepository.countBySearchTermPlusHidden(searchTerm);
			return hiddenCnt.intValue();
		}
	}
	
	// 프론트에 표시하기 위한 전체, 표시, 숨김 게시글수 조회(검색어유무에 따라 달라지도록 함)
		@Override
		public Map<String, Integer> getNoticeCounts(String searchTerm, String isHidden) throws Exception {
			
			System.out.println("검색어: " + searchTerm + ", 필터값: " + isHidden);
			
			Map<String, Integer> counts = new HashMap<>();
			Long totalCnt = noticeRepository.count();
			Long displayCnt = noticeRepository.countByIsHided("N");
			Long hiddenCnt = noticeRepository.countByIsHided("Y");

			// 검색수행시 새로 조회
			if(searchTerm!=null) {
				totalCnt = noticeDslRepository.countBySearchTerm(searchTerm);
				displayCnt = noticeDslRepository.countBySearchTermPlusDisplay(searchTerm);
				hiddenCnt = noticeDslRepository.countBySearchTermPlusHidden(searchTerm);
			} 
			
			counts.put("totalCnt", totalCnt.intValue());
			counts.put("displayCnt", displayCnt.intValue());
			counts.put("hiddenCnt", hiddenCnt.intValue());
			return counts;
		}
		
	// 공지사항 상세 조회
	@Override
	public Notice noticeDetail(Integer no) throws Exception {
		Optional<Notice> onotice = noticeRepository.findById(no);
		if(onotice.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Notice notice = onotice.get();
		return notice;
	}

	// 공지사항 목록 (검색, 필터, 페이징)
	@Override
	public List<Notice> noticeListBySearchAndFilterAndPaging(String sValue, String isHided, PageInfo pageInfo) throws Exception {
		// PageInfo를 PageRequest로 가공하여 Repository의 메서드를 호출
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<Notice> noticeList = noticeDslRepository.findNoticeListBySearchAndFilterAndPaging(sValue, isHided, pageRequest);
		
		System.out.println("***서비스 파라미터\nnoticeListsValue: " + sValue + "\nisHided: " + isHided);
		System.out.println("noticeList: " + noticeList);
		
		
		if(noticeList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		
		Integer allCount = noticeCntByCriteria(isHided, sValue);
		System.out.println("서비스에서 출력:\n필터유무와 검색어유무를 적용한 데이터 수: " + allCount);
		Integer allPage = (int) Math.ceil((double) allCount / pageRequest.getPageSize());
		if(allCount%pageRequest.getPageSize()!=0) allPage += 1;
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup * pagesPerGroup + 1);
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return noticeList;
	}

	
	
	
	
	
	
	

}
