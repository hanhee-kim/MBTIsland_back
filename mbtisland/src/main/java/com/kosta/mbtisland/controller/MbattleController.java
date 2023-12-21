package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.service.FileVoService;
import com.kosta.mbtisland.service.MbattleService;

@RestController
public class MbattleController {
	@Autowired
	private MbattleService mbattleService;
	
	@Autowired
	private FileVoService fileVoService;
	
	// 게시글 작성
	@PostMapping("/mbattlewrite")
	public ResponseEntity<Object> mbattleWrite(@ModelAttribute Mbattle mbattle, List<MultipartFile> files) {
		System.out.println(mbattle.getTitle());
		System.out.println("파일즈" + files);
		try {
			Integer no = mbattleService.insertMbattle(mbattle);
			
			String fileNums = fileVoService.insertFile("mbattle", no, files);
			System.out.println("파일명들:" + fileNums);
			String[] fileArr = fileNums.split(",");
			mbattle.setFileIdx1(fileArr[0]);
			mbattle.setFileIdx2(fileArr[1]);
			// 파일 인덱스를 수정하여 업데이트?
			mbattleService.insertMbattle(mbattle);
			
			Map<String, Object> res = new HashMap<>();
			res.put("no", no);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
