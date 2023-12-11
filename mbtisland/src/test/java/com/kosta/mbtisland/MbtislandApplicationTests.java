package com.kosta.mbtisland;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.service.MbtwhyServiceImpl;

@SpringBootTest
class MbtislandApplicationTests {
	
	@Autowired
	private MbtwhyServiceImpl mbtwhyServiceImpl;
	
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;

	@Test
	void contextLoads() {
	}
	
	@Test
	// Mbtwhy 게시글 목록 조회 (MBTI 타입)
	void selectMbtwhyByMbtiCategoryAndPage() throws Exception {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(1).build();
			List<Mbtwhy> mbtwhyList = mbtwhyServiceImpl.selectMbtwhyListByMbtiAndPageAndSearchAndSort("ENFP", pageInfo, null, null);
			for(int i = 0;i < mbtwhyList.size();i++) {
				System.out.println(mbtwhyList);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
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
				.content("나는 서울시 동작구의 김희찬이다. 꼬우면 찾아와라 현피뜨자!!!!!!!!!!!")
				.mbtiCategory("ISTJ")
//				.writeDate(stringToTimestamp)
				.writerId("user02")
				.writerNickname("토큰킴")
				.writerMbti("ISTP")
				.writerMbtiColor("#4D6879").build();
		
		mbtwhyServiceImpl.insertMbtwhy(mbtwhy);			

	}
	
	@Test
	@Commit
	void insetMbtwhyComment() throws Exception {
		MbtwhyComment mbtwhyComment = MbtwhyComment.builder()
				.commentContent("ㅋㅋ")
				.mbtwhyNo(12)
				.writerId("user02")
				.writerNickname("토큰킴")
				.writerMbti("ISTP")
				.writerMbtiColor("#4D6879").build();
		mbtwhyServiceImpl.insertMbtwhyComment(mbtwhyComment);
	}

}
