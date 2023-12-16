package com.kosta.mbtisland.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.AlarmDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.service.AlarmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmController {

	private final AlarmService alarmService;
//	alarmList
	
	@GetMapping("/alarmList")
	public ResponseEntity<Object> getAlarmList(@RequestParam String username,@RequestParam(required = false)String type,@RequestParam(required = false)Integer page){
		Map<String, Object> res = new HashMap<>();
		PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
		try {
			List<AlarmDto> alarmList = alarmService.getAlarmListByUserAndTypeAndPaging(username, type, pageInfo);
			res.put("alarmList", alarmList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<Object>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/updateAlarmIsRead")
	public ResponseEntity<Object> updateAlarmRead(@RequestParam String arrayItems){
		System.out.println("알람 선택 배열 읽기 컨트롤러");
		List<Integer> noList = Arrays.stream(arrayItems.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
		
		try {
			alarmService.updateAlarmRead(noList);
			return new ResponseEntity<Object>("읽음처리 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/updateAlarmIsReadAll")
	public ResponseEntity<Object> updateAlarmIsReadAll(@RequestParam String username){
		try {
			alarmService.updateAlarmReadAll(username);
			return new ResponseEntity<Object>("읽음처리 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
}
