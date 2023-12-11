package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.service.MbtwhyService;

@RestController
public class MbtwhyController {
	@Autowired
	private MbtwhyService mbtwhyService;
	
	// 게시글 페이징, 목록, 개수 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	@GetMapping("/mbtwhy")
	public ResponseEntity<Object> mbtwhyList(@RequestParam(required = false) String mbti, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
		Map<String, Object> res = new HashMap<>();
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<Mbtwhy> mbtwhyList = mbtwhyService.selectMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageInfo, search, sort);
			Long mbtwhyCnt = mbtwhyService.selectMbtwhyCountByMbtiAndSearch(mbti, search);
			System.out.println("페이지"+pageInfo);
			System.out.println("리스트"+mbtwhyList.get(0).getContent());
			System.out.println("카운트"+mbtwhyCnt);
			
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyList", mbtwhyList);
			res.put("mbtwhyCnt", mbtwhyCnt);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 게시글 조회 (게시글 번호)
	@GetMapping("/mbtwhydetail")
	public ResponseEntity<Object> mbtwhyList(@RequestParam(required = false) Integer no, @RequestParam(required = false) Integer commentPage) {
		System.out.println(no + ", " + commentPage);
		Map<String, Object> res = new HashMap<>();
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			Long mbtwhyCommentCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			System.out.println("페이지"+pageInfo);
			System.out.println("리스트"+mbtwhyCommentList.get(0).getCommentContent());
			System.out.println("카운트"+mbtwhyCommentCnt);
			
			res.put("pageInfo", pageInfo);
			res.put("mbtwhy", mbtwhy);
			res.put("mbtwhyCommentList", mbtwhyCommentList);
			res.put("mbtwhyCommentCnt", mbtwhyCommentCnt);
			
			System.out.println(mbtwhy.getWriterId());
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
