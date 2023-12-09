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
import com.kosta.mbtisland.entity.Mbtmi;
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
	

}
