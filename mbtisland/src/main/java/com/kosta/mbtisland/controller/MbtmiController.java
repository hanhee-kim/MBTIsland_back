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
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.entity.UserEntity;
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
											  , @RequestParam(required = false) String sort) {
		
		
		System.out.println("최신글목록 컨트롤러가 받은 파라미터(카테고리, 타입, 검색어, 페이지, 정렬): " + category + ", " + type + ", " + search + ", " + page + ", " + sort);
		
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
			List<MbtmiDto> mbtmiList = mbtmiService.mbtmiListByCategoryAndTypeAndSearch(category, type, search, pageInfo, sort);
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
	
	// 댓글 작성
	@PostMapping("/mbtmicomment")
	public ResponseEntity<Object> mbtmiDetailComment(@RequestBody UserEntity sendUser
													  , @RequestParam(required = false) Integer no
													  , @RequestParam(required = false) String comment
													  , @RequestParam(required = false) Integer parentcommentNo
													  , @RequestParam(required = false) Integer commentpage) {
		
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

			// 2. 알림데이터 삽입
			// alarmCnt로 할당할 값 조회: 게시글의 댓글 수 or 댓글의 대댓글 수
			Integer alarmCnt = 0;
			if(parentcommentNo==null) alarmCnt = mbtmiService.mbtmiCommentCnt(no);
			else alarmCnt = alarmCnt = mbtmiService.mbtmiChildCommentCnt(parentcommentNo);
			
			// alarmTargetNo && alarmTargetFrom이 같은 기존 데이터 유무를 조회하여 있다면 인서트가 아닌 업데이트 수행
			Alarm existAlarm = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(parentcommentNo==null? no : parentcommentNo, parentcommentNo==null? "mbtmi" : "mbtmiComment");
			
			if(existAlarm!=null) {
				existAlarm.setAlarmCnt(alarmCnt);
				alarmService.addAlarm(existAlarm);
			} else {
				Alarm alarm = Alarm.builder()
						.username(sendUser.getUsername())
						.alarmType("댓글")
						.alarmTargetNo(parentcommentNo==null? no : parentcommentNo)
						.alarmTargetFrom(parentcommentNo==null? "mbtmi" : "mbtmiComment")
						.alarmUpdateDate(writeDate)
						.alarmCnt(alarmCnt)
						.build();
				
				alarmService.addAlarm(alarm);
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
	
	
	// 게시글 작성
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
			
			// 추천 데이터 조회
			Recommend mbtmiRecommend = recommendService.selectRecommend(recommend.getUsername(), recommend.getPostNo(), recommend.getBoardType());
			
			if(mbtmiRecommend == null) {
				recommendService.insertRecommend(recommend);
				mbtmiService.increaseRecommendCnt(recommend.getPostNo());
			} else { 
				recommendService.deleteRecommend(mbtmiRecommend.getNo());
				mbtmiService.decreaseRecommendCnt(recommend.getPostNo());
			}
	
			// 게시글 및 추천수 재조회
			Mbtmi updatedMbtmi = mbtmiService.mbtmiDetail(recommend.getPostNo());
			Integer updatedRecommendCnt = mbtmi.getRecommendCnt();
			
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
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(bookmark.getPostNo());
			
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
	
	
	
}
