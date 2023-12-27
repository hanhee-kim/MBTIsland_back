package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.AnswerDto;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Answer;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.AnswerService;
import com.kosta.mbtisland.service.QuestionService;

@RestController
public class AnswerController {

	@Autowired
	private AnswerService answerService; 
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private QuestionService questionService;

	// 문의 답글 등록
	@PostMapping("/answerwrite")
	public ResponseEntity<Object> addAnswer(@RequestBody AnswerDto answerDto) {
		System.out.println("answerDto 출력: " + answerDto);
		try {
			// 답글데이터 삽입
			Answer writtenAnswer = answerService.addAnswer(answerDto);
			// 답변여부컬럼 업데이트
			questionService.changeIsAnswered(answerDto.getQuestionNo());
			// 알림데이터 삽입
			Alarm alarm = Alarm.builder()
					.username(answerDto.getQuestionWriterId()) // 알림의주인==문의글작성자
					.alarmType("문의답글")
					.alarmTargetNo(answerDto.getQuestionNo())
					.alarmTargetFrom("question")
					.alarmUpdateDate(answerDto.getWriteDate())
					.alarmCnt(1)
					.build();
			alarmService.addAlarm(alarm);
			
			Map<String, Object> res = new HashMap<>();
			res.put("answer", writtenAnswer);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
