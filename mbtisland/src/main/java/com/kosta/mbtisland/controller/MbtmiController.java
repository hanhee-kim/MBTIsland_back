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
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.service.MbtmiService;

@RestController
public class MbtmiController {
	
	@Autowired
	private MbtmiService mbtmiService;
	
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
	public ResponseEntity<Object> mbtmiDetail(@PathVariable Integer no) {
		try {
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(no); // 게시글
			mbtmiService.increaseViewCount(no); // 조회수 증가
			Integer mbtmiCommentCnt = mbtmiService.mbtmiCommentCnt(no); // 댓글수
			Map<String, Object> res = new HashMap<>();
	        res.put("mbtmi", mbtmi);
	        res.put("mbtmiCommentCnt", mbtmiCommentCnt);
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

/*
	// 추천
	@PostMapping("/mbtmirecommend")
	public ResponseEntity<Object> mbtmiDetailRecommend(@RequestBody Recommend recommend) {
		try {
			// 게시글 및 추천수 조회
			Mbtmi mbtmi = mbtmiService.mbtmiDetail(recommend.getPostNo());
			Integer recommendCnt = mbtmi.getRecommendCnt();
			
			// 추천 데이터 조회
//			Recommend mbtmiRecommend = mbtmiService.selectRecommendByUsernameAndPostNoAndBoardType(recommend.getUsername(), recommend.getPostNo(), recommend.getBoardType());
			
			if(mbtmiRecommend == null) { // 추천되지 않은 상태라면
				mbtmiService.insertRecommend(recommend); // 추천
				mbtmi.setRecommendCnt(recommendCnt + 1); // 추천수 + 1
				mbtmiService.insertMbtmi(mbtmi); // update
			} else { // 이미 추천된 상태라면
				mbtmiService.deleteRecommend(mbtmiRecommend.getNo()); // 추천 해제 (Recommend 테이블 PK로 delete)
				mbtmi.setRecommendCnt(recommendCnt - 1); // 추천수 - 1
				mbtmiService.insertMbtmi(mbtmi); // update
			}
			
			// 업데이트된 추천수 조회
			Integer mbtmiRecommendCount = mbtmiService.selectRecommendCountByPostNoAndBoardType(recommend.getPostNo(), recommend.getBoardType());
			
			// 추천수 반환
			Map<String, Object> res = new HashMap<>();
			res.put("mbtmiRecommendCount", mbtmiRecommendCount);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
*/
	
}
