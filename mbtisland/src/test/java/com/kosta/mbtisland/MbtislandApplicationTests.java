package com.kosta.mbtisland;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;
import com.kosta.mbtisland.repository.NoticeDslRepository;
import com.kosta.mbtisland.repository.NoticeRepository;
import com.kosta.mbtisland.service.NoticeService;

@SpringBootTest
class MbtislandApplicationTests {
	
	@Autowired
	NoticeRepository noticeRepository;
	@Autowired
	NoticeDslRepository noticeDslRepository;
	@Autowired
	NoticeService noticeService;

	@Test
	void contextLoads() {
	}

	
	/* 공지사항 */
	// 일괄 숨김/해제 처리
	@Test
	@Commit
	void noticeIsHidedUpdateBundle() throws Exception {
		Integer[] noArr = {1,2,4,5};
		noticeService.changeIsHided(noArr);
	}
	// 일괄 삭제 처리
	@Test
	@Commit
	void noticeDeleteBundle() throws Exception {
		Integer[] noArr = {21, 20};
		noticeService.deleteNotice(noArr);
	}
	
	// 공지사항 상세 조회
	@Test
	@Commit
	void noticeFindById() throws Exception {
		Integer no = 4;
		Notice notice = noticeService.noticeDetail(no);
		System.out.println(notice);
	}
	
	// 공지사항 전체/표시/숨김 개수 조회
	@Test
	@Commit
	void noticeCountBy() throws Exception {
		String filter = null; 
//		filter = "display";
//		filter = "hidden";
		Integer cnt = noticeService.noticeCntByCriteria(filter);
		System.out.println("결과: " + cnt);
	}

	// 공지사항 목록 (검색, 필터, 페이징)
	@Test
	void noticeFindBySearchAndFilterAndPaging() throws Exception {
		// 컨트롤러 (프론트로부터 현재페이지, 검색어, 필터값을 받음)
		Integer page = 1;
		PageInfo pageInfo = PageInfo.builder().curPage(page).build();
		String sValue = "더미"; // 문자열 또는 null
		String isHided = "Y"; // "Y", "N" 또는 null
		
		List<Notice> noticeList = noticeService.noticeListBySearchAndFilterAndPaging(sValue, isHided, pageInfo);
		Iterator iter = noticeList.iterator();
		System.out.println("---공지사항 목록 출력---");
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
	
	
	
	
	
	
	
	
	
}
