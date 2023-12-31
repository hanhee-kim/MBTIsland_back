package com.kosta.mbtisland;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.kosta.mbtisland.repository.AlarmRepository;
import com.kosta.mbtisland.repository.BookmarkDslRepository;
import com.kosta.mbtisland.repository.MbtmiCommentRepository;
import com.kosta.mbtisland.repository.MbtmiDslRepository;
import com.kosta.mbtisland.repository.MbtmiRepository;
import com.kosta.mbtisland.repository.MbtwhyDslRepository;
import com.kosta.mbtisland.repository.MbtwhyRepository;
import com.kosta.mbtisland.repository.NoteDslRepository;
import com.kosta.mbtisland.repository.NoticeDslRepository;
import com.kosta.mbtisland.repository.NoticeRepository;
import com.kosta.mbtisland.repository.QuestionRepository;
import com.kosta.mbtisland.repository.RecommendDslRepository;
import com.kosta.mbtisland.service.MbtmiService;
import com.kosta.mbtisland.service.MbtwhyServiceImpl;
import com.kosta.mbtisland.service.NoticeService;

@SpringBootTest
class MbtislandApplicationTests {

	// 하영
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private NoticeDslRepository noticeDslRepository;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MbtmiDslRepository mbtmiDslRepository;
	@Autowired
	private MbtmiRepository mbtmiRepository;
	@Autowired
	private MbtmiCommentRepository mbtmiCommentRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private MbtmiService mbtmiService;
	@Autowired
	private AlarmRepository alarmRepository;
	@Autowired
	private BookmarkDslRepository bookmarkDslRepository;
	@Autowired
	private RecommendDslRepository recommendDslRepository;

	// 인수
	@Autowired
	private MbtwhyServiceImpl mbtwhyServiceImpl;
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
	@Autowired
	private MbtwhyDslRepository mbtwhyDslRepository;


	//
	@Autowired
	private NoteDslRepository noteDslRepository;
	@Test
	void contextLoads() {
	}
	
	
//	/* 인수 */
//	
//	// MBTWhy
//	// Mbtwhy 게시글 목록 조회 (MBTI 타입)
//	@Test
//	void mbtwhyFindByMbtiCategoryAndPage() throws Exception {
//		try {
//			PageInfo pageInfo = PageInfo.builder().curPage(1).build();
//			List<MbtwhyDto> mbtwhyList = mbtwhyServiceImpl.selectMbtwhyListByMbtiAndPageAndSearchAndSort("ENFP", pageInfo, null, null);
//			for(int i = 0;i < mbtwhyList.size();i++) {
//				System.out.println(mbtwhyList);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	// Mbtwhy 일간 인기 게시글 조회 (MBTI)
//	@Test
//	void mbtwhyDailyHotMbtwhyFind() throws Exception {
//		try {
//			MbtwhyDto mbtwhyDto = mbtwhyServiceImpl.selectDailyHotMbtwhy("ISTJ");
//			System.out.println(mbtwhyDto.getWriteDate());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	// Mbtwhy 게시글 작성
//	@Test
//	@Commit
//	void mbtwhyInsert() throws Exception {
////		String currentTimestampToString = "2022/12/12 08:03:15";
//
//		//  String => Timestamp
////		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		
//		// 날짜 형식이 맞는지 확인하는 함수 setLenient()
//		// false로 설정해두면, 날짜 형식이 잘못 되었을 경우 해당 행에서 오류를 발생시킴
////		dateFormat.setLenient(false);
////		java.util.Date stringToDate = dateFormat.parse(currentTimestampToString);
////	    Timestamp stringToTimestamp = new Timestamp(stringToDate.getTime());
//		
//		Mbtwhy mbtwhy = Mbtwhy.builder()
//				.content("하이요!!!!!!!!!!!")
//				.mbtiCategory("ISTJ")
////				.writeDate(stringToTimestamp)
//				.writerId("asdf")
//				.writerNickname("asdf")
//				.writerMbti("ISTP")
//				.writerMbtiColor("#4D6879").build();
//		
//		mbtwhyServiceImpl.insertMbtwhy(mbtwhy);			
//
//	}
//	
//	// Mbtwhy 댓글 작성
//	@Test
//	@Commit
//	void mbtwhyCommentInsert() throws Exception {
//		MbtwhyComment mbtwhyComment = MbtwhyComment.builder()
//				.commentContent("ㅋㅋ")
//				.mbtwhyNo(14)
//				.writerId("user02")
//				.writerNickname("토큰킴")
//				.writerMbti("ISTP")
//				.writerMbtiColor("#4D6879").build();
//		mbtwhyServiceImpl.insertMbtwhyComment(mbtwhyComment);
//	}
//
//	
//	
//	
//	/* 하영 */
//	/* 공지사항 */
//	// 일괄 숨김/해제 처리
//	@Test
//	@Commit
//	void noticeIsHiddenUpdateBundle() throws Exception {
//		Integer[] noArr = {1,2,4,5};
//		noticeService.changeIsHidden(noArr);
//	}
//	// 일괄 삭제 처리
//	@Test
//	@Commit
//	void noticeDeleteBundle() throws Exception {
//		Integer[] noArr = {21, 20};
//		noticeService.deleteNotice(noArr);
//	}
//	
//	// 공지사항 상세 조회
//	@Test
//	@Commit
//	void noticeFindById() throws Exception {
//		Integer no = 4;
//		Notice notice = noticeService.noticeDetail(no);
//		System.out.println(notice);
//	}
//	
//	// 공지사항 전체/표시/숨김 개수 조회
//	@Test
//	@Commit
//	void noticeCountBy() throws Exception {
//		String filter = null; 
////		filter = "display";
////		filter = "hidden";
//		String searchTerm = null;
//		Integer cnt = noticeService.noticeCntByCriteria(filter, searchTerm);
//		System.out.println("결과: " + cnt);
//	}
//
//	// 공지사항 목록 (검색, 필터, 페이징)
//	@Test
//	void noticeFindBySearchAndFilterAndPaging() throws Exception {
//		// 컨트롤러 (프론트로부터 현재페이지, 검색어, 필터값을 받음)
//		Integer page = 1;
//		PageInfo pageInfo = PageInfo.builder().curPage(page).build();
//		String searchTerm = "더미"; // 문자열 또는 null
//		String isHidden = "Y"; // "Y", "N" 또는 null
//		
//		List<Notice> noticeList = noticeService.noticeListBySearchAndFilterAndPaging(searchTerm, isHidden, pageInfo);
//		Iterator<Notice> iter = noticeList.iterator();
//		System.out.println("---공지사항 목록 출력---");
//		while (iter.hasNext()) {
//			System.out.println(iter.next());
//		}
//	}
//	
//	// 공지사항 작성
//	@Test
//	@Commit
//	void noticeInsert() throws Exception {
//		// 컨트롤러 (프론트로부터 Notice객체를 받음)
//		Notice notice = Notice.builder().title("테스트코드 공지사항 제목").content("테스트코드 공지사항 내용").writerId("admin01").build();
//		noticeRepository.save(notice);
//		System.out.println("결과: " + notice);
//	}
//	
//	
//	// dsl검색에따른 데이터수 조회
//	@Test
//	void noticeCountBySearchTerm() throws Exception {
//		String searchTerm = "숨긴";
//		Long totalCnt = noticeDslRepository.countBySearchTerm(searchTerm);
//		System.out.println("결과: " + totalCnt);
//	}
//	
//	
//	/* 문의 */
//	@Test
//	void questionFindByWriterId() {
//		String writerId = "user05";
//		List<Question> questionList = questionRepository.findByWriterId(writerId);
//		Iterator<Question> iter = questionList.iterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
//	}
//	
// 	
//	/* mbtmi */
//	@Test
//	void mbtmiWeeklyHotList() throws Exception {
//		List<Tuple> maxRecommendCntByCategory = mbtmiDslRepository.findMaxRecommendCntByCategory();
//		System.out.println("카테고리별 최다추천수 조회: ");
//		Iterator<Tuple> iter1 = maxRecommendCntByCategory.iterator();
//		while(iter1.hasNext()) {
//			System.out.println(iter1.next());
//		}
//		
//		List<Mbtmi> weeklyHotList = mbtmiDslRepository.findWeeklyHotMbtmiListByCategoryAndRecommendCnt();
//		System.out.println("카테고리별 최다추천수를 가진 게시글행 조회");
//		Iterator<Mbtmi> iter2 = weeklyHotList.iterator();
//		while(iter2.hasNext()) {
//			System.out.println(iter2.next());
//		}
//	}
//	
//	@Test
//	void mbtmiNewlyMbtmiList() throws Exception {
//		// 컨트롤러
//		String category = null;
//		String type = null;
//		String searchTerm = null;
//		Integer page = 1;
//		String sort = null;
//		String username = "react03"; // 마이페이지 리스트 조회를 위해 파라미터 추가
//		PageInfo pageInfo = PageInfo.builder().curPage(page).build();
//		// 서비스
//		Integer itemsPerPage = 10;
//		int pagesPerGroup = 10;
//		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
//		
//		List<Mbtmi> newlyMbtmiList = mbtmiDslRepository.findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(category, type, searchTerm, pageRequest, sort, username);
//		System.out.println("------최신글 목록 출력------");
//		Iterator<Mbtmi> iter = newlyMbtmiList.iterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
//	}
//	
//	
//	@Test
//	void mbtwhyCntBy() {
//		try {
//			Integer cnt = mbtwhyRepository.countByWriterId("121212");
//			System.out.println("결과 : "+ cnt);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	@Test
//	void mbtmiCommentList() throws Exception {
//		// 컨트롤러
//		Integer mbtmiNo = 240;
//		Integer page = 1;
//		PageInfo pageInfo = PageInfo.builder().curPage(page).build();
//		// 서비스
//		Integer itemsPerPage = 10;
//		int pagesPerGroup = 10;
//		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
//		
//		List<MbtmiComment> mbtmiCommentList = mbtmiDslRepository.findMbtmiCommentListByMbtmiNoAndPaging(mbtmiNo, pageRequest);
//		System.out.println("------댓글 목록 출력------");
//		Iterator<MbtmiComment> iter = mbtmiCommentList.iterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
//	}
//
//
//	// 특정게시글의 댓글수
//	@Test
//	void mbtmiCommentCntByMbtmiNo() throws Exception {
//		Integer mbtmiNo = 240;
//		Integer commentCnt = mbtmiService.mbtmiCommentCnt(mbtmiNo);
//		System.out.println("결과: " + commentCnt);
//	}
//
//	
//	
//	//-----------------------
//	void tupleList() throws Exception{
//		System.out.println("test중");
//		Integer page = 1;
//		PageInfo pageInfo = PageInfo.builder().curPage(page).build();
//		
//		Integer itemsPerPage = 10;
//		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
////		List<Tuple> noteDtoList = noteDslRepository.test("Kakao_3152074495", "N", pageRequest);
////		List<Tuple> noteDtoList = noteDslRepository.test("Kakao_3152074495", "N");
////		List<Note> noteDtoList = noteDslRepository.test2("Kakao_3152074495", "N");
////		List<Note> noteDtoList = noteDslRepository.test3("Kakao_3152074495");
////		List<NoteDto> noteDtoList = noteDslRepository.findNoteListBySentUserAndAndReadTypeAndPaging("Kakao_3152074495", "N", pageRequest);
////		System.out.println(noteDtoList.size());
////		for(NoteDto note : noteDtoList) {	
////			System.out.println(note);
////		}
//	}
	
	
//	@Test
//	@Commit
//	void mbtmiCommentDeleteByMbtmiNo() throws Exception {
//		Integer mbtmiNo = 317;
//		mbtmiDslRepository.deleteCommentsByMbtmiNo(mbtmiNo);
//	}

	@Test
	@Commit
	void recommendDeleteByPostNoAndBoardType() throws Exception {
		Integer postNo = 323;
		String boardType = "MBTMI";
		recommendDslRepository.deleteRecommendByPostNoAndBoardType(postNo, boardType);
	}
	
	@Test
	void findCommentNosByPostNo() {
		Integer postNo = 1;
		List<Integer> nos = mbtmiDslRepository.findCommentNosByPostNo(postNo);
		System.out.println("결과------------");
		for (Integer no : nos) {
			System.out.println(no);
		}
	}
	
	@Test
	@Commit
	void deleteMbtmi() throws Exception {
		Integer mbtmiNo = 317;
		mbtmiService.deleteMbtmi(mbtmiNo);
	}
	
	
	
}
	