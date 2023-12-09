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
import com.kosta.mbtisland.entity.Question;
import com.kosta.mbtisland.service.QuestionService;

@RestController
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	// 문의글 목록
	@GetMapping("/questionlist")
	public ResponseEntity<Object> questionList(@RequestParam(required = false) String search
											 , @RequestParam(required = false) String answered
											 , @RequestParam(required = false) Integer page) {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
			List<Question> questionList = questionService.questionListBySearchAndFilterAndPaging(search, answered, pageInfo);
			Map<String, Integer> questionCnts = questionService.getQuestionCounts(search, answered);
			
			Map<String, Object> res = new HashMap<>();
	        res.put("pageInfo", pageInfo);
	        res.put("questionList", questionList);
	        res.put("questionCnts", questionCnts);
	        return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
