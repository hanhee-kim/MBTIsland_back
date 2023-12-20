package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.service.MbattleService;

@RestController
public class MbattleController {
	@Autowired
	private MbattleService mbattleService;
	
	// 게시글 작성
	@PostMapping("/mbattlewrite")
	public ResponseEntity<Object> mbattleWrite(@RequestBody Mbattle mbattle) {
		
		try {
//			LocalDate currentDate = LocalDate.now();
//			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
//			
//			Mbtwhy mbtwhy = Mbtwhy.builder()
//					.content(content)
//					.mbtiCategory(mbti)
//					.writerId(sendUser.getUsername())
//					.writerNickname(sendUser.getUserNickname())
//					.writerMbti(sendUser.getUserMbti())
//					.writerMbtiColor(sendUser.getUserMbtiColor())
//					.writeDate(writeDate).build();
			
			// 게시글 삽입과 동시에 해당 컬럼에서 auto increment로 생성되는 id인 no 반환
			Integer no = mbattleService.insertMbattle(mbattle);
			Map<String, Object> res = new HashMap<>();
			res.put("no", no);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
