package com.kosta.mbtisland;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.service.MbtwhyServiceImpl;

@SpringBootTest
class MbtislandApplicationTests {
	
	@Autowired
	MbtwhyServiceImpl mbtwhyServiceImpl;

	@Test
	void contextLoads() {
	}
	
	@Test
	@Commit
	// Mbtwhy 게시글 작성
	void insertMbtwhy() throws Exception {
//		String currentTimestampToString = "2022/12/12 08:03:15";
//
//		//  String => Timestamp
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		
//		// 날짜 형식이 맞는지 확인하는 함수 setLenient()
//		// false로 설정해두면, 날짜 형식이 잘못 되었을 경우 해당 행에서 오류를 발생시킴
//		dateFormat.setLenient(false);
//		
//		java.util.Date stringToDate = dateFormat.parse(currentTimestampToString);
//	    Timestamp stringToTimestamp = new Timestamp(stringToDate.getTime());
		
		Mbtwhy mbtwhy = Mbtwhy.builder()
				.content("ㅎㅇ")
				.mbtiCategory("ENFP")
//				.writeDate(stringToTimestamp)
				.writerId("user01")
				.writerNickname("닉네임1")
				.writerMbti("ISFP")
				.writerMbtiColor("#618181").build();
		
		mbtwhyServiceImpl.insertMbtwhy(mbtwhy);
	}

}
