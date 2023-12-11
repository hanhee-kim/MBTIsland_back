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
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.service.MbtmiService;

@RestController
public class MbtmiController {
	
	@Autowired
	private MbtmiService mbtmiService;
	
	// 주간인기글 목록
	@GetMapping("/weeklyhotmbtmi")
	public ResponseEntity<Object> weeklyHotMbtmiList() {
		try {
			List<Mbtmi> weeklyHotMbtmiList = mbtmiService.weeklyHotMbtmiList();
			return new ResponseEntity<Object>(weeklyHotMbtmiList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 최신글 목록
	@GetMapping("/mbtmilist")
	public ResponseEntity<Object> mbtmiList(@RequestParam(required = false) String category
											  , @RequestParam(required = false) String type
											  , @RequestParam(required = false) String search
											  , @RequestParam(required = false) Integer page) {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
			List<Mbtmi> mbtmiList = mbtmiService.mbtmiListByCategoryAndTypeAndSearch(category, type, search, pageInfo);
			Map<String, Object> res = new HashMap<>();
	        res.put("pageInfo", pageInfo);
	        res.put("mbtmiList", mbtmiList);
	        return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 상세
	@GetMapping("/mbtmidetail/{no}")
	public ResponseEntity<Object> noticeDetail(@PathVariable Integer no, @RequestParam(required = false) Integer commentPage) {
		try {
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(no);
			mbtmiService.increaseViewCount(no);
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1: commentPage).build();
			List<MbtmiComment> mbtmiCommentList = mbtmiService.mbtmiCommentListByMbtmiNo(no, pageInfo);
			Integer mbtmiCommentCnt = mbtmiService.mbtmiCommentCnt(no);
			Map<String, Object> res = new HashMap<>();
	        res.put("mbtmi", mbtmi);
	        res.put("mbtmiCommentList", mbtmiCommentList);
	        res.put("mbtmiCommentCnt", mbtmiCommentCnt);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 삭제
	@DeleteMapping("/deletembtmi/{no}")
	public ResponseEntity<Object> deleteMbtmi(@PathVariable Integer no) {
		try {
			mbtmiService.deleteMbtmi(no);
			return new ResponseEntity<Object>(no + " 삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
