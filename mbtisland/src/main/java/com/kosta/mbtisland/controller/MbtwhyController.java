package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.MbtwhyCommentRepository;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.BookmarkService;
import com.kosta.mbtisland.service.MbtwhyService;
import com.kosta.mbtisland.service.RecommendService;

@RestController
public class MbtwhyController {
	@Autowired
	private MbtwhyService mbtwhyService;
	
	@Autowired
	private RecommendService recommendService;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private MbtwhyCommentRepository mbtwhyCommentRepository;
	
	// 게시글 페이징, 게시글 목록, 인기 게시글, 개수 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	@GetMapping("/mbtwhy")
	public ResponseEntity<Object> mbtwhyList(@RequestParam(required = false) String mbti, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<MbtwhyDto> mbtwhyList = mbtwhyService.selectMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageInfo, search, sort);
			MbtwhyDto hotMbtwhy = mbtwhyService.selectDailyHotMbtwhy(mbti);
//			Long mbtwhyCnt = mbtwhyService.selectMbtwhyCountByMbtiAndSearch(mbti, search);
			
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyList", mbtwhyList);
			res.put("hotMbtwhy", hotMbtwhy);
//			res.put("mbtwhyCnt", mbtwhyCnt);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 조회 (게시글 번호, 댓글 페이지, 유저 아이디)
	@GetMapping("/mbtwhydetail")
	public ResponseEntity<Object> mbtwhyDetail(@RequestParam(required = false) Integer no, @RequestParam(required = false) String username) {
		try {
			// 조회수 증가
			mbtwhyService.increaseViewCount(no);
			// Mbtwhy 게시글 (추천 수 포함하므로, 게시글 처음 보여질 때는 해당 GetMapping에서 추천수 가져와서 사용)
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			// 추천 여부 조회
			Boolean isMbtwhyRecommended = recommendService.selectIsRecommendByUsernameAndPostNoAndBoardType(username, no, "mbtwhy");
			// 북마크 여부 조회
			Boolean isMbtwhyBookmarked = bookmarkService.selectIsBookmarkByUsernameAndPostNoAndBoardType(username, no, "mbtwhy");
			
			Map<String, Object> res = new HashMap<>();
			res.put("mbtwhy", mbtwhy);
			res.put("isMbtwhyRecommended", isMbtwhyRecommended);
			res.put("isMbtwhyBookmarked", isMbtwhyBookmarked);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 작성
	@PostMapping("/mbtwhywrite")
	public ResponseEntity<Object> mbtwhyWrite(@RequestBody UserEntity sendUser,
			@RequestParam(required = false) String mbti, @RequestParam(required = false) String content) {
		
		try {
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			
			Mbtwhy mbtwhy = Mbtwhy.builder()
					.content(content)
					.mbtiCategory(mbti)
					.writerId(sendUser.getUsername())
					.writerNickname(sendUser.getUserNickname())
					.writerMbti(sendUser.getUserMbti())
					.writerMbtiColor(sendUser.getUserMbtiColor())
					.writeDate(writeDate).build();
			
			// 게시글 삽입과 동시에 해당 컬럼에서 auto increment로 생성되는 id인 no 반환
			Integer no = mbtwhyService.insertMbtwhy(mbtwhy);
			Map<String, Object> res = new HashMap<>();
			res.put("no", no);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 수정폼 조회
	@GetMapping("/getmbtwhymodify/{no}")
	public ResponseEntity<Object> getMbtwhyModify(@PathVariable Integer no) {
		try {
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			Map<String, Object> res = new HashMap<>();
			res.put("mbtiCategory", mbtwhy.getMbtiCategory());
			res.put("content", mbtwhy.getContent());
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 수정
	@PostMapping("/mbtwhymodify/{no}/{content}")
	public ResponseEntity<Object> mbtwhyModify(@PathVariable Integer no, @PathVariable String content) {
		try {
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			mbtwhy.setContent(content);
			mbtwhyService.insertMbtwhy(mbtwhy);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 삭제
	@DeleteMapping("/mbtwhydelete/{no}")
	public ResponseEntity<Object> mbtwhyDelete(@PathVariable Integer no) {
		try {
			mbtwhyService.deleteMbtwhy(no);
			return new ResponseEntity<Object>(no + " 삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 추천 or 추천 취소
	// 매개변수로 가져온 Recommend와 동일한 컬럼이 있다면 delete, 없다면 insert
	@PostMapping("/mbtwhyrecommend")
	public ResponseEntity<Object> mbtwhyDetailRecommend(@RequestBody Recommend recommend) {
		try {
			// 게시글 및 추천수 조회
//			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(recommend.getPostNo());
			
			// 추천 데이터 조회
			Recommend mbtwhyRecommend = recommendService.selectRecommend(recommend.getUsername(), recommend.getPostNo(), recommend.getBoardType());
			
			if(mbtwhyRecommend == null) { // 추천되지 않은 상태라면
				recommendService.insertRecommend(recommend); // 추천
				mbtwhyService.increaseRecommendCnt(recommend.getPostNo()); // 추천수 + 1
			} else { // 이미 추천된 상태라면
				recommendService.deleteRecommend(mbtwhyRecommend.getNo()); // 추천 해제 (Recommend 테이블 PK로 delete)
				mbtwhyService.decreaseRecommendCnt(recommend.getPostNo()); // 추천수 - 1
			}
			
			// 업데이트된 추천수 조회
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(recommend.getPostNo());
			Integer updatedRecommendCount = mbtwhy.getRecommendCnt();
			
			// 추천수 반환
			Map<String, Object> res = new HashMap<>();
			res.put("mbtwhyRecommendCount", updatedRecommendCount);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 북마크 or 북마크 취소
	// 매개변수로 가져온 Bookmark와 동일한 컬럼이 있다면 delete, 없다면 insert
	@PostMapping("mbtwhybookmark")
	public void mbtwhyDetailBookmark(@RequestBody Bookmark bookmark) {
		try {
			// 북마크 데이터 조회
			Bookmark mbtwhyBookMark = bookmarkService.selectBookmark(bookmark.getUsername(), bookmark.getPostNo(), bookmark.getBoardType());

			if (mbtwhyBookMark == null) { // 추천되지 않은 상태라면
				bookmarkService.insertBookmark(bookmark); // 북마크
			} else { // 이미 추천된 상태라면
				bookmarkService.deleteBookmark(mbtwhyBookMark.getNo()); // 북마크 해제 (Bookmark 테이블 PK로 delete)
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 댓글 목록 조회 (게시글 번호, 댓글 페이지)
	@GetMapping("mbtwhycommentlist/{no}")
	public ResponseEntity<Object> mbtwhyCommentList(@PathVariable Integer no,
			@RequestParam(required = false) Integer commentPage) {

		try {
			// 페이징 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage == null ? 1 : commentPage).build();
			// 댓글목록
			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 댓글 개수
			Integer mbtwhyCommentCount = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			Map<String, Object> res = new HashMap<>();

			res.put("mbtwhyCommentList", mbtwhyCommentList);
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyCommentCount", mbtwhyCommentCount);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
	// 댓글 작성
	@PostMapping("/mbtwhycomment")
	public ResponseEntity<Object> mbtwhyDetailComment(@RequestBody UserEntity sendUser,
			@RequestParam(required = false) Integer no, @RequestParam(required = false) String comment,
			@RequestParam(required = false) Integer parentcommentNo, @RequestParam(required = false) Integer commentPage) {
		
		try {
			// 1. 댓글 삽입
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			// 댓글 Entity 빌드
			MbtwhyComment mbtwhyComment = MbtwhyComment.builder()
					.commentContent(comment)
					.mbtwhyNo(no)
					.parentcommentNo(parentcommentNo)
					.writerId(sendUser.getUsername())
					.writerNickname(sendUser.getUserNickname())
					.writerMbti(sendUser.getUserMbti())
					.writerMbtiColor(sendUser.getUserMbtiColor())
					.writeDate(writeDate)
					.build();

			mbtwhyService.insertMbtwhyComment(mbtwhyComment);
			
			// 2. 알림 데이터 처리
			// 2-1. 1차댓글 등록의 경우
			if (parentcommentNo == null) {
				Alarm alarmForPostWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(no, "mbtwhy");

				Integer alarmCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no); // alarmCnt컬럼값
				String username = mbtwhyService.selectMbtwhyByNo(no).getWriterId(); // 알림의 주인==게시글작성자

				// 알림 처리 제외 대상에 해당하는지 여부(게시글작성자 본인의 댓글인지 여부)
				Boolean isWrittenByOneSelf = username.equals(sendUser.getUsername());

				// 알림의 존재여부에 따라 alarmCnt컬럼값만 업데이트 수행* or 알림데이터 인서트 수행**
				if (alarmForPostWriter != null && !isWrittenByOneSelf) {
					alarmForPostWriter.setAlarmCnt(alarmCnt);
					alarmForPostWriter.setAlarmIsRead("N");
					alarmForPostWriter.setAlarmReadDate(null);
					alarmService.addAlarm(alarmForPostWriter); // *
				} else if (alarmForPostWriter == null && !isWrittenByOneSelf) {
					Alarm alarm = Alarm.builder().username(username).alarmType("댓글").alarmTargetNo(no)
							.alarmTargetFrom("mbtwhy").alarmUpdateDate(writeDate).alarmCnt(alarmCnt).build();
					alarmService.addAlarm(alarm); // **
				}

			// 2-2. 2차댓글 등록의 경우
			} else {
				Alarm alarmForParentcommentWriter = alarmService
						.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(parentcommentNo, "mbtwhy");
				Alarm alarmForPostWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(no, "mbtwhy");

				Integer alarmCnt1 = mbtwhyService.selectMbtwhyChildCommentCount(parentcommentNo); // 알림Cnt1
				String username1 = mbtwhyCommentRepository.findById(parentcommentNo).get().getWriterId(); // 알림의
																											// 주인1==1차댓글의
																											// 작성자
				Integer alarmCnt2 = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no); // 알림Cnt2
				String username2 = mbtwhyService.selectMbtwhyByNo(no).getWriterId(); // 알림의 주인2==게시글작성자

				// 알림 처리 제외 대상에 해당하는지 여부(게시글작성자 본인의 2차댓글인지, 1차댓글작성자 본인의 2차댓글인지 여부)
				Boolean isWrittenByParentcommentWriter = username1.equals(sendUser.getUsername());
				Boolean isWrittenByPostWriter = username2.equals(sendUser.getUsername());

				// 2-2-1. 1차댓글 작성자를 향한 알림데이터 업데이트 또는 인서트
				if (alarmForParentcommentWriter != null && !isWrittenByParentcommentWriter) {
					alarmForParentcommentWriter.setAlarmCnt(alarmCnt1);
					alarmForParentcommentWriter.setAlarmIsRead("N");
					alarmForParentcommentWriter.setAlarmReadDate(null);
					alarmService.addAlarm(alarmForParentcommentWriter); // alarmCnt컬럼값만 업데이트 수행
				} else if (alarmForParentcommentWriter == null && !isWrittenByParentcommentWriter) {
					Alarm alarm1 = Alarm.builder().username(username1).alarmType("댓글").alarmTargetNo(parentcommentNo)
							.alarmTargetFrom("mbtwhy").alarmUpdateDate(writeDate).alarmCnt(alarmCnt1).build();
					alarmService.addAlarm(alarm1); // 인서트 수행
				}

				// 2-2-2. 게시글 작성자를 향한 알림데이터 업데이트
				if (alarmForPostWriter != null && !isWrittenByPostWriter) {
					alarmForPostWriter.setAlarmCnt(alarmCnt2);
					alarmService.addAlarm(alarmForPostWriter); // alarmCnt컬럼값만 업데이트 수행
				}
			}
			
			// 페이지 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// 게시글 댓글 목록
			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 게시글 댓글 수
			Integer mbtwhyCommentCount = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			
			// 삽입된 댓글
			Integer writtenCommentNo = mbtwhyComment.getCommentNo();
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyCommentList", mbtwhyCommentList);
			res.put("mbtwhyCommentCount", mbtwhyCommentCount);
			res.put("writtenCommentNo", writtenCommentNo);
			System.out.println(pageInfo);
			System.out.println("올페이지" + pageInfo.getAllPage());
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 삭제(IS_REMOVED 컬럼값 업데이트)
	@GetMapping("/mbtwhycommentdelete/{commentNo}")
	public ResponseEntity<String> deleteMbtwhyComment(@PathVariable Integer commentNo) {
		try {
			mbtwhyService.deleteMbtwhyComment(commentNo);
			return new ResponseEntity<String>("삭제된 댓글입니다", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// Mypage에서 myMbt-Why 최초 로드시
	@GetMapping("/mymbtwhy/{username}/{page}")//@RequestParam usename?
	public ResponseEntity<Map<String,Object>> myMbtwhyList(@PathVariable String username,@PathVariable(required = false) Integer page){
		System.out.println("myYLIstController진입");
		System.out.println(page);
		System.out.println(username);
		PageInfo pageInfo = new PageInfo(page);
		pageInfo.setCurPage(page==null? 1:page);
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			List<Mbtwhy> myMbtwhyList = mbtwhyService.getMyMbtwhyListByPage(username, pageInfo);
			res.put("myMbtwhyList", myMbtwhyList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("errMsg", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.BAD_REQUEST);
		}
	}
	
	// 배열의 No 삭제
	@DeleteMapping("/deletembtwhy")				//@RequestBody Map<String,String> param
	public ResponseEntity<Object> deleteMbtwhyList(@RequestParam String sendArrayItems){
		System.out.println("delete컨트롤러 진입");
		System.out.println(sendArrayItems);
		//숫자 형태의 리스트로 변환
		List<Integer> noList = Arrays.stream(sendArrayItems.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
		try {
			mbtwhyService.deleteMbtwhyList(noList);
			return new ResponseEntity<Object>("삭제 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("삭제 실패",HttpStatus.OK);
		}
		
	}

}
