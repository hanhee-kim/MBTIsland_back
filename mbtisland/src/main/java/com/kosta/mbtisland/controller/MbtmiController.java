package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kosta.mbtisland.dto.MbtmiDto;
import com.kosta.mbtisland.dto.NoticeDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.MbtmiCommentRepository;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.BookmarkService;
import com.kosta.mbtisland.service.MbtmiService;
import com.kosta.mbtisland.service.RecommendService;

@RestController
public class MbtmiController {
	
	@Autowired
	private MbtmiService mbtmiService;
	@Autowired
	private RecommendService recommendService;
	@Autowired
	private BookmarkService bookmarkService;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private MbtmiCommentRepository mbtmiCommentRepository;
	
	// 주간인기글 목록
	@GetMapping("/weeklyhotmbtmi")
	public ResponseEntity<Object> weeklyHotMbtmiList() {
		try {
			List<MbtmiDto> weeklyHotMbtmiList = mbtmiService.weeklyHotMbtmiList();
			Map<String, Object> res = new HashMap<>();
			res.put("weeklyHotMbtmiList", weeklyHotMbtmiList);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 최신글 목록
	@GetMapping("/mbtmilist")
	public ResponseEntity<Object> mbtmiList(@RequestParam(required = false) String category
											  , @RequestParam(required = false) String type
											  , @RequestParam(required = false) String search
											  , @RequestParam(required = false) Integer page
											  , @RequestParam(required = false) String sort
											  , @RequestParam(required = false) String username) {
		
		
//		System.out.println("최신글목록 컨트롤러가 받은 파라미터(카테고리, 타입, 검색어, 페이지, 정렬, 작성자): " + category + ", " + type + ", " + search + ", " + page + ", " + sort + ", " + username);
		
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
//			List<MbtmiDto> mbtmiList = mbtmiService.mbtmiListByCategoryAndTypeAndSearch(category, type, search, pageInfo, sort);
			List<MbtmiDto> mbtmiList = mbtmiService.mbtmiListByCategoryAndTypeAndSearch(category, type, search, pageInfo, sort, username);
			Map<String, Object> res = new HashMap<>();
	        res.put("pageInfo", pageInfo);
	        res.put("mbtmiList", mbtmiList);
	        return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 상세
	@GetMapping("/mbtmidetail/{no}")
//	public ResponseEntity<Object> mbtmiDetail(@PathVariable Integer no, @RequestParam(required = false) Integer commentPage) {
	public ResponseEntity<Object> mbtmiDetail(@PathVariable Integer no, @RequestParam(required = false) String username) {
		try {
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(no); // 게시글
			mbtmiService.increaseViewCount(no); // 조회수 증가
			Integer mbtmiCommentCnt = mbtmiService.mbtmiCommentCnt(no); // 댓글수
			Boolean isMbtmiRecommend = recommendService.selectIsRecommendByUsernameAndPostNoAndBoardType(username, no, "mbtmi"); // 추천여부
			Boolean isMbtmiBookmark = bookmarkService.selectIsBookmarkByUsernameAndPostNoAndBoardType(username, no, "mbtmi"); // 북마크여부
			Map<String, Object> res = new HashMap<>();
	        res.put("mbtmi", mbtmi);
	        res.put("mbtmiCommentCnt", mbtmiCommentCnt);
	        res.put("isMbtmiRecommend", isMbtmiRecommend);
	        res.put("isMbtmiBookmark", isMbtmiBookmark);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 삭제
	@DeleteMapping("/deletembtmi/{no}")
	public ResponseEntity<Object> deleteMbtmi(@PathVariable Integer no) {
		try {
			mbtmiService.deleteMbtmi(no);
			return new ResponseEntity<Object>(no + " 삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 목록
	@GetMapping("/mbtmicommentlist/{no}")
	public ResponseEntity<Object> mbtmiCommentList(@PathVariable Integer no, @RequestParam(required = false) Integer commentpage) {
		try {
//			System.out.println("댓글목록 컨트롤러가 받은 파라미터: " + no + ", " + commentpage);
			PageInfo pageInfo = PageInfo.builder().curPage(commentpage==null? 1: commentpage).build();
			List<MbtmiComment> mbtmiCommentList = mbtmiService.mbtmiCommentListByMbtmiNo(no, pageInfo); // 댓글목록
			Map<String, Object> res = new HashMap<>();
	        res.put("mbtmiCommentList", mbtmiCommentList);
	        res.put("pageInfo", pageInfo);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 삭제(IS_REMOVED 컬럼값 업데이트)
	@GetMapping("/deletembtmicomment/{commentNo}")
	public ResponseEntity<String> deleteMbtmiComment(@PathVariable Integer commentNo) {
		System.out.println("댓글삭제 컨트롤러 호출");
		try {
			mbtmiService.deleteMbtmiComment(commentNo);
			return new ResponseEntity<String>("삭제된 댓글입니다", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 등록
	@PostMapping("/mbtmicomment")
	public ResponseEntity<Object> mbtmiDetailComment(@RequestBody UserEntity sendUser
													  , @RequestParam(required = false) Integer no
													  , @RequestParam(required = false) String comment
													  , @RequestParam(required = false) Integer parentcommentNo
													  , @RequestParam(required = false) Integer commentpage) {
		
		
		System.out.println("=======댓글등록 컨트롤러에서 출력=======");
		System.out.println("sendUser: " + sendUser);
		System.out.println("no(게시글번호): " + no);
		System.out.println("comment(댓글내용): " + comment);
		System.out.println("1차댓글번호: " + parentcommentNo);
		System.out.println("commentPage: " + commentpage);
		
		try {
			// 1. 댓글 삽입
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			MbtmiComment mbtmiComment = MbtmiComment.builder()
										.commentContent(comment)
										.mbtmiNo(no)
										.parentcommentNo(parentcommentNo)
										.writerId(sendUser.getUsername())
										.writerNickname(sendUser.getUserNickname())
										.writerMbti(sendUser.getUserMbti())
										.writerMbtiColor(sendUser.getUserMbtiColor())
										.writeDate(writeDate)
										.build();
			mbtmiService.addMbtmiComment(mbtmiComment);

			// 2. 알림데이터 처리
			// 2-1. 1차댓글 등록의 경우
			if(parentcommentNo==null) {
				Alarm alarmForPostWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(no, "mbtmi");

				Integer alarmCnt = mbtmiService.mbtmiCommentCnt(no); // alarmCnt컬럼값
				String username = mbtmiService.mbtmiDetail(no).getWriterId(); // 알림의 주인==게시글작성자

				// 알림의 존재여부에 따라 alarmCnt컬럼값만 업데이트 수행* or 알림데이터 인서트 수행**
				if(alarmForPostWriter!=null) {
					alarmForPostWriter.setAlarmCnt(alarmCnt);
					alarmService.addAlarm(alarmForPostWriter); // *
				} else {
					Alarm alarm = Alarm.builder()
							.username(username)
							.alarmType("댓글")
							.alarmTargetNo(no)
							.alarmTargetFrom("mbtmi")
							.alarmUpdateDate(writeDate)
							.alarmCnt(alarmCnt)
							.build();
					alarmService.addAlarm(alarm); // **
				}
				
			// 2-2. 2차댓글 등록의 경우
			} else {
				Alarm alarmForParentcommentWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(parentcommentNo, "mbtmiComment");
				Alarm alarmForPostWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(no, "mbtmi");
				
				Integer alarmCnt1 = mbtmiService.mbtmiChildCommentCnt(parentcommentNo); // 알림Cnt1
				String username1 = mbtmiCommentRepository.findById(parentcommentNo).get().getWriterId(); // 알림의 주인1==1차댓글의 작성자
				Integer alarmCnt2 = mbtmiService.mbtmiCommentCnt(no); // 알림Cnt2
//				String username2 = mbtmiService.mbtmiDetail(no).getWriterId(); // 알림의 주인2==게시글작성자
				
				// 2-2-1. target을 1차댓글로 하는 알림데이터 업데이트 또는 인서트
				if(alarmForParentcommentWriter!=null) {
					alarmForParentcommentWriter.setAlarmCnt(alarmCnt1);
					alarmService.addAlarm(alarmForParentcommentWriter); // alarmCnt컬럼값만 업데이트 수행
				} else {
					Alarm alarm1 = Alarm.builder()
							.username(username1)
							.alarmType("댓글")
							.alarmTargetNo(parentcommentNo)
							.alarmTargetFrom("mbtmiComment")
							.alarmUpdateDate(writeDate)
							.alarmCnt(alarmCnt1)
							.build();
					alarmService.addAlarm(alarm1); // 인서트 수행
				}
				
				// 2-2-2. target을 게시글로 하는 알림데이터 업데이트
				if(alarmForPostWriter!=null) {
					alarmForPostWriter.setAlarmCnt(alarmCnt2);
					alarmService.addAlarm(alarmForPostWriter); // alarmCnt컬럼값만 업데이트 수행
				}
			}
			
			
			PageInfo pageInfo = PageInfo.builder().curPage(commentpage==null? 1: commentpage).build();
			List<MbtmiComment> mbtmiCommentList = mbtmiService.mbtmiCommentListByMbtmiNo(no, pageInfo);
			Integer mbtmiCommentCnt = mbtmiService.mbtmiCommentCnt(no);
			
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbtmiCommentList", mbtmiCommentList);
			res.put("mbtmiCommentCnt", mbtmiCommentCnt);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	// 게시글 등록
	@PostMapping("/mbtmiwrite")
	public ResponseEntity<Object> addPost(@RequestBody MbtmiDto mbtmiDto) {
//		System.out.println("게시글작성 컨트롤러가 받은 파라미터 mbtmiDto: " + mbtmiDto);
		
		try {
			Mbtmi mbtmi = mbtmiService.addMbtmi(mbtmiDto);
			Map<String, Object> res = new HashMap<>();
			res.put("mbtmi", mbtmi);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// 추천
	@PostMapping("/mbtmirecommend")
	public ResponseEntity<Object> mbtmiDetailRecommend(@RequestBody Recommend recommend) {
		try {
			// 게시글 및 추천수 조회
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(recommend.getPostNo());
			Integer recommendCnt = mbtmi.getRecommendCnt();
//			System.out.println("이전 추천수: " + recommendCnt);
			
			// 추천 데이터 조회
			Recommend mbtmiRecommend = recommendService.selectRecommend(recommend.getUsername(), recommend.getPostNo(), recommend.getBoardType());
			
			if(mbtmiRecommend == null) {
				recommendService.insertRecommend(recommend);
				mbtmiService.increaseRecommendCnt(recommend.getPostNo());
			} else { 
				recommendService.deleteRecommend(mbtmiRecommend.getNo());
				mbtmiService.decreaseRecommendCnt(recommend.getPostNo());
			}
	
			// 추천수 재조회
			Integer updatedRecommendCnt = mbtmi.getRecommendCnt();
//			System.out.println("현재 추천수: " + updatedRecommendCnt);
			
			Map<String, Object> res = new HashMap<>();
			res.put("mbtmiRecommendCount", updatedRecommendCnt);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 북마크
	@PostMapping("/mbtmibookmark")
	public ResponseEntity<Object> mbtmiDetailBookmark(@RequestBody Bookmark bookmark) {
		try {
			// 게시글 조회
//			Mbtmi mbtmi = mbtmiService.mbtmiDetail(bookmark.getPostNo());
			
			// 북마크 데이터 조회
			Bookmark mbtmiBookmark = bookmarkService.selectBookmark(bookmark.getUsername(), bookmark.getPostNo(), bookmark.getBoardType());
			
			if(mbtmiBookmark == null) {
				bookmarkService.insertBookmark(bookmark);
			} else { 
				bookmarkService.deleteBookmark(mbtmiBookmark.getNo());
			}
	
			// 게시글 재조회
//			Mbtmi updatedMbtmi = mbtmiService.mbtmiDetail(bookmark.getPostNo());
			return new ResponseEntity<Object>("북마크/해제 성공", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 수정
	@PostMapping("/mbtmimodify")
	public ResponseEntity<Object> modifyMbtmi (@RequestBody MbtmiDto mbtmiDto) {
		System.out.println("수정컨트롤러가 받은 파라미터 mbtmiDto: " + mbtmiDto);
		try {
			Mbtmi modifiedMbtmi = mbtmiService.modifyMbtmi(mbtmiDto);
			Map<String, Object> res = new HashMap<>();
			res.put("notice", modifiedMbtmi);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
}
