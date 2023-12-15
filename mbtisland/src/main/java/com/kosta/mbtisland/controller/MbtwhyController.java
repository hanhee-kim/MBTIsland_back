package com.kosta.mbtisland.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
			System.out.println("MBTI 타입은 " + mbti);
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
//			System.out.println("리스트"+mbtwhyCommentList.get(0).getCommentContent());
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
	
	
	
	
	
	
	
	//Mypage에서 myMbt-Why 최초 로드시
	@GetMapping("/mymbtwhy/{username}/{page}")//@RequestParam usename?
	public ResponseEntity<Map<String,Object>> myMbtwhyList(@PathVariable String username,@PathVariable(required = false) Integer page){
		System.out.println("myYLIstController진입");
		System.out.println(page);
		System.out.println(username);
		PageInfo pageInfo = new PageInfo(page);
		pageInfo.setCurPage(page==null? 1:page);
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			List<Mbtwhy> myMbtwhyList = mbtwhyService.getMyMbtwhyListByPage(username, pageInfo);
			res.put("myMbtwhyList", myMbtwhyList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("errMsg", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.BAD_REQUEST);
		}
	}
	
	//배열의 No 삭제
	@DeleteMapping("/deletembtwhy")				//@RequestBody Map<String,String> param
	public ResponseEntity<Object> deleteMbtwhyList(@RequestParam String sendArrayItems){
		System.out.println("delete컨트롤러 진입");
		System.out.println(sendArrayItems);
		//숫자 형태의 리스트로 변환
		List<Integer> noList = Arrays.stream(sendArrayItems.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
//		for(Integer no : noList) {
//			System.out.println(no);
//		}
		try {
			mbtwhyService.updateIsRemoved(noList);
			return new ResponseEntity<Object>("삭제컬럼 변경 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("삭제 변경 실패",HttpStatus.OK);
		}
		
	}
	
	
	
	
	
	
}
