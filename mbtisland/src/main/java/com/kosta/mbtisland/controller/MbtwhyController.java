package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.MbtwhyDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.service.MbtwhyService;

@RestController
public class MbtwhyController {
	@Autowired
	private MbtwhyService mbtwhyService;
	
	// 게시글 페이징, 게시글 목록, 인기 게시글, 개수 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	@GetMapping("/mbtwhy")
	public ResponseEntity<Object> mbtwhyList(@RequestParam(required = false) String mbti, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
		Map<String, Object> res = new HashMap<>();
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<MbtwhyDto> mbtwhyList = mbtwhyService.selectMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageInfo, search, sort);
			MbtwhyDto mbtwhyHot = mbtwhyService.selectDailyHotMbtwhy(mbti);
//			Long mbtwhyCnt = mbtwhyService.selectMbtwhyCountByMbtiAndSearch(mbti, search);
			
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyList", mbtwhyList);
			res.put("mbtwhyHot", mbtwhyHot);
//			res.put("mbtwhyCnt", mbtwhyCnt);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 게시글 조회 (게시글 번호)
	@GetMapping("/mbtwhydetail")
	public ResponseEntity<Object> mbtwhyDetail(@RequestParam(required = false) Integer no, @RequestParam(required = false) Integer commentPage) {
		Map<String, Object> res = new HashMap<>();
		try {
			// 페이지 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// Mbtwhy 게시글
			MbtwhyDto mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			// 게시글 댓글 목록
			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 게시글 댓글 수
			Integer mbtwhyCommentCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			
			res.put("pageInfo", pageInfo);
			res.put("mbtwhy", mbtwhy);
			res.put("mbtwhyCommentList", mbtwhyCommentList);
			res.put("mbtwhyCommentCnt", mbtwhyCommentCnt);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 게시글 작성
	@PostMapping("/mbtwhywrite")
	public ResponseEntity<Object> mbtwhyWrite(@RequestParam(required = false) String mbti,
			@RequestParam(required = false) String content, @RequestBody UserEntity user) {
		Map<String, Object> res = new HashMap<>();
		LocalDate currentDate = LocalDate.now();
		Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
		
		try {
			Mbtwhy mbtwhy = Mbtwhy.builder()
					.content(content)
					.mbtiCategory(mbti)
					.writerId(user.getUsername())
					.writerNickname(user.getUserNickname())
					.writerMbti(user.getUserMbti())
					.writerMbtiColor(user.getUserMbtiColor())
					.writeDate(writeDate).build();
			
			// 게시글 삽입과 동시에 해당 컬럼에서 auto increment로 생성되는 id인 no 반환받음
			Integer no = mbtwhyService.insertMbtwhy(mbtwhy);
			res.put("no", no);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 댓글 작성
	@PostMapping("/mbtwhydetail")
	public ResponseEntity<Object> mbtwhyDetail(@RequestParam(required = false) Integer no,
			@RequestParam(required = false) String comment, @RequestParam(required = false) Integer parentcommentNo,
			@RequestParam(required = false) Integer commentPage, @RequestBody UserEntity userInfo) {
		Map<String, Object> res = new HashMap<>();
		LocalDate currentDate = LocalDate.now();
		Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
		
		System.out.println("zzzzzzzzzzzz");
	
		try {
			// 댓글 Entity 빌드
			MbtwhyComment mbtwhyComment = MbtwhyComment.builder()
					.commentContent(comment)
					.mbtwhyNo(no)
					.parentcommentNo(parentcommentNo)
					.writerId(userInfo.getUsername())
					.writerNickname(userInfo.getUserNickname())
					.writerMbti(userInfo.getUserMbti())
					.writerMbtiColor(userInfo.getUserMbtiColor())
					.writeDate(writeDate)
					.build();
			// 댓글 삽입
			mbtwhyService.insertMbtwhyComment(mbtwhyComment);
			
			// 페이지 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// 게시글 댓글 목록
			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 게시글 댓글 수
			Integer mbtwhyCommentCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyCommentList", mbtwhyCommentList);
			res.put("mbtwhyCommentCnt", mbtwhyCommentCnt);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
