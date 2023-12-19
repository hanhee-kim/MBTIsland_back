package com.kosta.mbtisland.controller;

import java.util.Arrays;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.kosta.mbtisland.entity.Recommend;
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
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<MbtwhyDto> mbtwhyList = mbtwhyService.selectMbtwhyListByMbtiAndPageAndSearchAndSort(mbti, pageInfo, search, sort);
			MbtwhyDto mbtwhyHot = mbtwhyService.selectDailyHotMbtwhy(mbti);
//			Long mbtwhyCnt = mbtwhyService.selectMbtwhyCountByMbtiAndSearch(mbti, search);
			
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbtwhyList", mbtwhyList);
			res.put("mbtwhyHot", mbtwhyHot);
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
			// 페이지 정보
//			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// 조회수 증가
			mbtwhyService.increaseViewCount(no);
			// Mbtwhy 게시글 (추천 수 포함하므로, 게시글 처음 보여질 때는 해당 GetMapping에서 추천수 가져와서 사용)
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(no);
			// 게시글 댓글 목록
//			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 게시글 댓글 수
//			Integer mbtwhyCommentCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			// 추천 여부
			Boolean isMbtwhyRecommended = mbtwhyService.selectIsRecommendByUsernameAndPostNoAndBoardType(username, no, "mbtwhy");
			// 북마크 여부 조회
			Boolean isMbtwhyBookmarked = mbtwhyService.selectIsBookmarkByUsernameAndPostNoAndBoardType(username, no, "mbtwhy");
			
			Map<String, Object> res = new HashMap<>();
//			res.put("pageInfo", pageInfo);
			res.put("mbtwhy", mbtwhy);
//			res.put("mbtwhyCommentList", mbtwhyCommentList);
//			res.put("mbtwhyCommentCnt", mbtwhyCommentCnt);
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
			@RequestParam(required = false) Integer parentcommentNo) {
		
		try {
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

			// 댓글 삽입
			mbtwhyService.insertMbtwhyComment(mbtwhyComment);
			
			// 페이지 정보
//			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// 게시글 댓글 목록
//			List<MbtwhyComment> mbtwhyCommentList = mbtwhyService.selectMbtwhyCommentListByMbtwhyNoAndPage(no, pageInfo);
			// 게시글 댓글 수
//			Integer mbtwhyCommentCnt = mbtwhyService.selectMbtwhyCommentCountByMbtwhyNo(no);
			
//			Map<String, Object> res = new HashMap<>();
//			res.put("pageInfo", pageInfo);
//			res.put("mbtwhyCommentList", mbtwhyCommentList);
//			res.put("mbtwhyCommentCnt", mbtwhyCommentCnt);
			
			return new ResponseEntity<Object>(HttpStatus.OK);
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
	
	// 게시글 추천 or 추천 취소
	// 매개변수로 가져온 Recommend와 동일한 컬럼이 있다면 delete, 없다면 insert
	@PostMapping("/mbtwhyrecommend")
	public ResponseEntity<Object> mbtwhyDetailRecommend(@RequestBody Recommend recommend) {
		try {
			// 게시글 및 추천수 조회
			Mbtwhy mbtwhy = mbtwhyService.selectMbtwhyByNo(recommend.getPostNo());
			Integer recommendCnt = mbtwhy.getRecommendCnt();
			
			// 추천 데이터 조회
			Recommend mbtwhyRecommend = mbtwhyService.selectRecommendByUsernameAndPostNoAndBoardType(recommend.getUsername(), recommend.getPostNo(), recommend.getBoardType());
			
			if(mbtwhyRecommend == null) { // 추천되지 않은 상태라면
				mbtwhyService.insertRecommend(recommend); // 추천
				mbtwhy.setRecommendCnt(recommendCnt + 1); // 추천수 + 1
				mbtwhyService.insertMbtwhy(mbtwhy); // update
			} else { // 이미 추천된 상태라면
				mbtwhyService.deleteRecommend(mbtwhyRecommend.getNo()); // 추천 해제 (Recommend 테이블 PK로 delete)
				mbtwhy.setRecommendCnt(recommendCnt - 1); // 추천수 - 1
				mbtwhyService.insertMbtwhy(mbtwhy); // update
			}
			
			// 업데이트된 추천수 조회
			Integer mbtwhyRecommendCount = mbtwhyService.selectRecommendCountByPostNoAndBoardType(recommend.getPostNo(), recommend.getBoardType());
			
			// 추천수 반환
			Map<String, Object> res = new HashMap<>();
			res.put("mbtwhyRecommendCount", mbtwhyRecommendCount);
			
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
		
		System.out.println(bookmark.getBoardType());
		System.out.println(bookmark.getPostNo());
		System.out.println(bookmark.getUsername());
		try {
			// 북마크 데이터 조회
			Bookmark mbtwhyBookMark = mbtwhyService.selectBookmarkByUsernameAndPostNoAndBoardType(bookmark.getUsername(), bookmark.getPostNo(), bookmark.getBoardType());

			if (mbtwhyBookMark == null) { // 추천되지 않은 상태라면
				mbtwhyService.insertBookmark(bookmark); // 북마크
			} else { // 이미 추천된 상태라면
				mbtwhyService.deleteBookmark(mbtwhyBookMark.getNo()); // 북마크 해제 (Bookmark 테이블 PK로 delete)
			}
		} catch (Exception e) {
			e.printStackTrace();
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
//		for(Integer no : noList) {
//			System.out.println(no);
//		}
		try {
			mbtwhyService.updateIsRemoved(noList);
			return new ResponseEntity<Object>("삭제컬럼 변경 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("삭제 변경 실패",HttpStatus.OK);
		}
		
	}

}
