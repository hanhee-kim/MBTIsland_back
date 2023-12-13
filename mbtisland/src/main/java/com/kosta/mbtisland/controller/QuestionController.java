package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Answer;
import com.kosta.mbtisland.entity.Question;
import com.kosta.mbtisland.service.AnswerService;
import com.kosta.mbtisland.service.QuestionService;

@RestController
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
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
	
	// 특정 유저의 문의글 모아보기
	@GetMapping("/questionlistofuser")
	public ResponseEntity<Object> questionListOfUser(@RequestParam String user
													, @RequestParam(required = false) String answered
													, @RequestParam(required = false) Integer page) {
		System.out.println("문의글 불러오기 컨트롤러");
		System.out.println("user:"+user+"answer:"+answered+"page:"+page);
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
			List<Question> questionList = questionService.questionListByUserAndFilterAndPaging(user, answered, pageInfo);
			Map<String, Integer> questionCnts = questionService.getQuestionCountsByUser(user, answered);
			
			Map<String, Object> res = new HashMap<>();
	        res.put("pageInfo", pageInfo);
	        res.put("questionList", questionList);
	        res.put("questionCnts", questionCnts);
	        Question a = questionList.get(0);
	        System.out.println("---------------------------------------------");
	        System.out.println(a.getWriteDate());
	        return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//문의글 등록
	@PostMapping("questionwrite")
	public ResponseEntity<String> questionWrite(@RequestBody Question question){
		System.out.println("문의글 등록 컨트롤러 진입");
		try {
			questionService.questionWrite(question);
			return new ResponseEntity<String>("문의글 등록 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("문의글 등록 실패", HttpStatus.BAD_REQUEST);
		}
	}
	
	//문의글 상세보기
	@GetMapping("/questiondetail/{no}")
	public ResponseEntity<Object> questionDetailByNo(@PathVariable Integer no){
		System.out.println("문의디테일넘버컨트롤러");
		Map<String, Object> res = new HashMap<>();
		try {
			Question question = questionService.questionDetailByNo(no);
			Optional<Answer> answer = answerService.findByQuestionNo(no);
			res.put("question", question);
			if(answer.isPresent()) {
				res.put("answer", answer.get());
			}
			return new ResponseEntity<Object>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
