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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.AlarmDto;
import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.NoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmController {

	private final AlarmService alarmService;
	private final NoteService noteservice;
//	alarmList
	
	@GetMapping("/alarmList")
	public ResponseEntity<Object> getAlarmList(@RequestParam String username,@RequestParam(required = false)String type,@RequestParam(required = false)Integer page){
		Map<String, Object> res = new HashMap<>();
		if(username!=null)System.out.println(username);
		if(type!=null)System.out.println(type);
		if(page!=null)System.out.println(page);
		PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
		try {
			List<AlarmDto> alarmList = alarmService.getAlarmListByUserAndTypeAndPaging(username, type, pageInfo);
			res.put("alarmList", alarmList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<Object>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("err", e.getMessage());
			res.put("alarmList", null);
			return new ResponseEntity<Object>(res,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/updatealarmisread")
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
	
	@PutMapping("/updatealarmisreadall")
	public ResponseEntity<Object> updateAlarmIsReadAll(@RequestParam String username){
		try {
			alarmService.updateAlarmReadAll(username);
			return new ResponseEntity<Object>("읽음처리 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getnoteandalarm")
	public ResponseEntity<Object> getNoteAndAlarm(@RequestParam String username){
		Map<String, Object> res = new HashMap<>();
		try {
			List<AlarmDto> alarmList = alarmService.getAlarmListByAlarmIsNotReadBy5(username);
			Long alarmCnt = alarmService.getCntNotReadAlarmList(username);
			res.put("alarmList", alarmList);
			res.put("alarmCnt", alarmCnt);
			List<NoteDto> noteDtoList = noteservice.getNoteListNotReadByUsername(username);
			Long noteCnt = noteservice.getCntNotReadNoteList(username);
			res.put("noteList", noteDtoList);
			res.put("noteCnt", noteCnt);
			return new ResponseEntity<Object>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/checkalarm/{no}")
	public ResponseEntity<Object> checkAlarm(@PathVariable Integer no){
		try {
			alarmService.updateAlarmRead(no);
			return new ResponseEntity<Object>("읽음처리성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
}
