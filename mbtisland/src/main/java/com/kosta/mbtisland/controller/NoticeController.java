package com.kosta.mbtisland.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Notice;
import com.kosta.mbtisland.service.NoticeService;

@RestController
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;

	// 공지사항 목록
	@GetMapping("/noticelist")
	public ResponseEntity<Object> noticeList(@RequestParam(required = false) String search
														, @RequestParam(required = false) String hidden
														, @RequestParam(required = false) Integer page) {
		
		System.out.println("***컨트롤러가 받은 파라미터 출력-----");
		System.out.println("search: " + search);
		System.out.println("hidden: " + hidden);
		System.out.println("page: " + page);
		
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
			List<Notice> noticeList = noticeService.noticeListBySearchAndFilterAndPaging(search, hidden, pageInfo);
			Map<String, Integer> noticeCnts = noticeService.getNoticeCounts(search, hidden);
			System.out.println(noticeCnts);
			
			Map<String, Object> res = new HashMap<>();
	        res.put("pageInfo", pageInfo);
	        res.put("noticeList", noticeList);
	        res.put("noticeCnts", noticeCnts);
	        return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 공지사항 일괄 숨김/해제 처리
	@GetMapping("/hidenotice/{noArr}")
	public ResponseEntity<Object> hideNoticeBundle(@PathVariable Integer[] noArr) {
		try {
			noticeService.changeIsHided(noArr);
			return new ResponseEntity<Object>(Arrays.toString(noArr) + " 의 일괄처리 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	// 공지사항 일괄 삭제 처리
	@DeleteMapping("/deletenotice/{noArr}")
	public ResponseEntity<Object> deleteNoticeBundle(@PathVariable Integer[] noArr) {
		try {
			noticeService.deleteNotice(noArr);
			return new ResponseEntity<Object>(Arrays.toString(noArr) + " 의 일괄삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
