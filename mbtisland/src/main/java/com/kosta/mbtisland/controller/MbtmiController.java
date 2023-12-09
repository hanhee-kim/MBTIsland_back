package com.kosta.mbtisland.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	

}
