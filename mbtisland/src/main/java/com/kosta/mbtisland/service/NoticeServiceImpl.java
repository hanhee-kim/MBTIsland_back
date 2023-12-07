package com.kosta.mbtisland.service;

import java.util.List;
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

	// 게시글수 조회
	@Override
	public Integer noticeCntByCriteria(String filter) throws Exception {
		Long totalCnt = 0L;
		Long displayCnt = 0L;
		Long hideCnt = 0L;
		
		if(filter==null) {
			totalCnt = noticeRepository.count();
			return totalCnt.intValue();
		} else if(filter.equals("display")) {
			displayCnt = noticeRepository.countByIsHided("N");
			return displayCnt.intValue();
		} else { // filter == hidden
			hideCnt = noticeRepository.countByIsHided("Y");
			return hideCnt.intValue();
		}
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
		if(noticeList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		return noticeList;
	}
	
	
	
	
	
	

}
