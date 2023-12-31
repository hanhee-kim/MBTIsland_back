package com.kosta.mbtisland.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.mbtisland.dto.MbattleDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleComment;
import com.kosta.mbtisland.entity.MbattleResult;
import com.kosta.mbtisland.entity.MbattleVoter;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.BookmarkService;
import com.kosta.mbtisland.service.FileVoService;
import com.kosta.mbtisland.service.MbattleService;

@RestController
public class MbattleController {
	@Autowired
	private MbattleService mbattleService;
	
	@Autowired
	private FileVoService fileVoService;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private AlarmService alarmService;
	
	// 게시글 페이징, 게시글 목록, 인기 게시글, 개수 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	@GetMapping("/mbattle")
	public ResponseEntity<Object> mbattleList(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) String search, @RequestParam(required = false) String sort) {
		try {
			PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1 : page).build();
			List<MbattleDto> mbattleList = mbattleService.selectMbattleListByPageAndSearchAndSort(pageInfo, search, sort);
			List<Mbattle> hotMbattleList= mbattleService.selectDailyHotMbattle();
			System.out.println("앰배틀");
			System.out.println(hotMbattleList);
			
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbattleList", mbattleList);
			res.put("hotMbattleList", hotMbattleList);				
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 조회 (게시글 번호, 댓글 페이지, 유저 아이디)
	@GetMapping("/mbattledetail/{no}")
	public ResponseEntity<Object> mbattleDetail(@PathVariable Integer no, @RequestParam(required = false) String username) {
		try {
			
			// 조회수 증가
			mbattleService.increaseViewCount(no);
			// Mbattle 게시글 (추천 수 포함하므로, 게시글 처음 보여질 때는 해당 GetMapping에서 추천수 가져와서 사용)
			Mbattle mbattle = mbattleService.selectMbattleByNo(no);
			// 투표 데이터 조회 (투표 여부)
			MbattleVoter mbattleVoter = mbattleService.selectMbattleVoterByUsernameAndPostNo(username, no);
			// 북마크 여부 조회
			Boolean isMbattleBookmarked = bookmarkService.selectIsBookmarkByUsernameAndPostNoAndBoardType(username, no, "mbattle");
			// 투표 결과 조회
			MbattleResult mbattleResult1 = mbattleService.selectMbattleResultByMbattleNoAndVoteItem(no, 1);
			MbattleResult mbattleResult2 = mbattleService.selectMbattleResultByMbattleNoAndVoteItem(no, 2);
			System.out.println(mbattleResult1);
			System.out.println(mbattleResult2);
			
			Map<String, Object> res = new HashMap<>();
			res.put("mbattle", mbattle);
			res.put("mbattleVoter", mbattleVoter);
			res.put("isMbattleBookmarked", isMbattleBookmarked);
			res.put("mbattleResult1", mbattleResult1);
			res.put("mbattleResult2", mbattleResult2);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 이미지 출력
	@GetMapping("/mbattleimg/{fileIdx}")
	public void imageView(@PathVariable Integer fileIdx, HttpServletResponse response) {
		try {
			fileVoService.readImage(fileIdx, response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 게시글 작성
	@PostMapping("/mbattlewrite")
	public ResponseEntity<Object> mbattleWrite(@ModelAttribute Mbattle mbattle, List<MultipartFile> files) {
		try {
			Integer no = mbattleService.insertMbattle(mbattle);
			
			if(files != null) {				
				String fileNums = fileVoService.insertFile("mbattle", no, files);
				System.out.println("파일명들:" + fileNums);
				String[] fileArr = fileNums.split(",");
				// 파일 인덱스를 수정
				mbattle.setFileIdx1(fileArr[0]);
				mbattle.setFileIdx2(fileArr[1]);
			} else {
				mbattle.setFileIdx1(null);
				mbattle.setFileIdx2(null);
			}
			mbattleService.insertMbattle(mbattle);
			
			Map<String, Object> res = new HashMap<>();
			res.put("no", no);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 삭제
	@DeleteMapping("/mbattledelete/{no}")
	public ResponseEntity<Object> mbattleDelete(@PathVariable Integer no) {
		try {
			mbattleService.deleteMbattle(no);
			return new ResponseEntity<Object>(no + " 삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 게시글 북마크 or 북마크 취소
	// 매개변수로 가져온 Bookmark와 동일한 컬럼이 있다면 delete, 없다면 insert
	@PostMapping("mbattlebookmark")
	public void mbattleDetailBookmark(@RequestBody Bookmark bookmark) {
		try {
			// 북마크 데이터 조회
			Bookmark mbattleBookMark = bookmarkService.selectBookmark(bookmark.getUsername(), bookmark.getPostNo(), bookmark.getBoardType());

			if (mbattleBookMark == null) { // 추천되지 않은 상태라면
				bookmarkService.insertBookmark(bookmark); // 북마크
			} else { // 이미 추천된 상태라면
				bookmarkService.deleteBookmark(mbattleBookMark.getNo()); // 북마크 해제 (Bookmark 테이블 PK로 delete)
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 랜덤 게시글 번호 조회
	@GetMapping("/mbattlerandom")
	public ResponseEntity<Object> mbattleRandom() {
		try {
			Integer randomNo = mbattleService.selectRandomMbattleNo();
			Map<String, Object> res = new HashMap<>();
			System.out.println("랜덤값: " + randomNo);
			res.put("randomNo", randomNo);

			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 목록 조회 (게시글 번호, 댓글 페이지)
	@GetMapping("mbattlecommentlist/{no}")
	public ResponseEntity<Object> mbattleCommentList(@PathVariable Integer no,
			@RequestParam(required = false) Integer commentPage) {

		try {
			// 페이징 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage == null ? 1 : commentPage).build();
			// 댓글목록
			List<MbattleComment> mbattleCommentList = mbattleService.selectMbattleCommentListByMbattleNoAndPage(no, pageInfo);
			// 댓글 개수
			Integer mbattleCommentCount = mbattleService.selectMbattleCommentCountByMbattleNo(no);
			Map<String, Object> res = new HashMap<>();

			res.put("mbattleCommentList", mbattleCommentList);
			res.put("pageInfo", pageInfo);
			res.put("mbattleCommentCount", mbattleCommentCount);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
	// 댓글 작성
	@PostMapping("/mbattlecomment")
	public ResponseEntity<Object> mbattleDetailComment(@RequestBody UserEntity sendUser,
			@RequestParam(required = false) Integer no, @RequestParam(required = false) String comment,
			@RequestParam(required = false) Integer parentcommentNo, @RequestParam(required = false) Integer commentPage) {
		
		try {
			// 1. 댓글 삽입
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			Timestamp writeDate2 = new Timestamp(new Date().getTime()); // java.util.Date
			// 댓글 Entity 빌드
			MbattleComment mbattleComment = MbattleComment.builder()
					.commentContent(comment)
					.mbattleNo(no)
					.writerId(sendUser.getUsername())
					.writerNickname(sendUser.getUserNickname())
					.writerMbti(sendUser.getUserMbti())
					.writerMbtiColor(sendUser.getUserMbtiColor())
					.writeDate(writeDate)
					.build();

			mbattleService.insertMbattleComment(mbattleComment);
			
			// 2. 알림 데이터 처리
			if (parentcommentNo == null) {
				Alarm alarmForPostWriter = alarmService.selectAlarmByAlarmTargetNoAndAlarmTargetFrom(no, "mbattle");

//				Integer alarmCnt = mbattleService.selectMbattleCommentCountByMbattleNo(no); // (alarmCnt 컬럼값) - (게시글이 가진 댓글수)
				Integer alarmCnt = 0;
				if(alarmForPostWriter != null) {
					alarmCnt = alarmForPostWriter.getAlarmCnt() + 1; // (alarmCnt 컬럼값) - (게시글이 가진 댓글 수)
				} else {
					alarmCnt = 1;
				}
				String username = mbattleService.selectMbattleByNo(no).getWriterId(); // 알림의 주인 == 게시글작성자

				// 알림 처리 제외 대상에 해당하는지 여부 (게시글 작성자 본인의 댓글인지 여부)
				Boolean isWrittenByOneSelf = username.equals(sendUser.getUsername());

				// 알림의 존재여부에 따라 alarmCnt컬럼값만 업데이트 수행* or 알림데이터 인서트 수행**
				if (alarmForPostWriter != null && !isWrittenByOneSelf) {
					alarmForPostWriter.setAlarmCnt(alarmCnt);
					alarmForPostWriter.setAlarmIsRead("N");
					alarmForPostWriter.setAlarmReadDate(null);
					alarmForPostWriter.setAlarmUpdateDate(writeDate2);
					alarmService.addAlarm(alarmForPostWriter); // *
				} else if (alarmForPostWriter == null && !isWrittenByOneSelf) {
					Alarm alarm = Alarm.builder()
							.username(username)
							.alarmType("댓글")
							.alarmTargetNo(no)
							.alarmTargetFrom("mbattle")
							.alarmUpdateDate(writeDate)
							.alarmCnt(alarmCnt)
							.build();
					alarmService.addAlarm(alarm); // **
				}
			}
			
			// 페이지 정보
			PageInfo pageInfo = PageInfo.builder().curPage(commentPage==null? 1 : commentPage).build();
			// 게시글 댓글 목록
			List<MbattleComment> mbattleCommentList = mbattleService.selectMbattleCommentListByMbattleNoAndPage(no, pageInfo);
			// 게시글 댓글 수
			Integer mbattleCommentCount = mbattleService.selectMbattleCommentCountByMbattleNo(no);
			
			// 삽입된 댓글
			Integer writtenCommentNo = mbattleComment.getCommentNo();
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbattleCommentList", mbattleCommentList);
			res.put("mbattleCommentCount", mbattleCommentCount);
			res.put("writtenCommentNo", writtenCommentNo);
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 댓글 삭제(IS_REMOVED 컬럼값 업데이트)
	@GetMapping("/mbattlecommentdelete/{commentNo}")
	public ResponseEntity<String> deleteMbattleComment(@PathVariable Integer commentNo) {
		try {
			mbattleService.deleteMbattleComment(commentNo);
			return new ResponseEntity<String>("삭제된 댓글입니다", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// 투표
	@PostMapping("/mbattlevote/{voterMbti}/{vote}")
	public ResponseEntity<Object> voteMbattleItem(@RequestBody MbattleVoter voter,
			@PathVariable String voterMbti, @PathVariable Integer vote) {
		try {
			// mbattleVoter 테이블에 투표 여부 삽입 (게시글 번호, 유저 아이디, 투표 항목)
			voter.setVoteItem(vote);
			mbattleService.insertMbattleVoter(voter);

			// mbattleResult 조회
			MbattleResult mbattleResult = mbattleService.selectMbattleResultByMbattleNoAndVoteItem(voter.getMbattleNo(), voter.getVoteItem());
			
			// 조회 결과가 없다면, 새로 생성
			if(mbattleResult == null) {
				mbattleResult = new MbattleResult();
				// 게시글 번호 set
				mbattleResult.setMbattleNo(voter.getMbattleNo());
				// 투표 항목 set
				mbattleResult.setVoteItem(voter.getVoteItem());
				
				// 총 투표수 set
				mbattleResult.setVoteCnt(1);
				
				// mbti 분류 결과 set
				if(voterMbti.charAt(0)=='I'){
					mbattleResult.setI(1);
				} else if(voterMbti.charAt(0)=='E') {
					mbattleResult.setE(1);
				}
				
				if(voterMbti.charAt(1)=='S'){
					mbattleResult.setS(1);
				} else if(voterMbti.charAt(1)=='N') {
					mbattleResult.setN(1);
				}

				if(voterMbti.charAt(2)=='T'){
					mbattleResult.setT(1);
				} else if(voterMbti.charAt(2)=='F') {
					mbattleResult.setF(1);
				}
				
				if(voterMbti.charAt(3)=='J'){
					mbattleResult.setJ(1);
				} else if(voterMbti.charAt(3)=='P') {
					mbattleResult.setP(1);
				}
			} else { // 조회 결과가 있다면
				mbattleResult.setVoteCnt(mbattleResult.getVoteCnt() + 1);
				// mbti 분류 결과 set
				if(voterMbti.charAt(0)=='I'){
					mbattleResult.setI(mbattleResult.getI() + 1);						
				} else if(voterMbti.charAt(0)=='E') {
					mbattleResult.setE(mbattleResult.getE() + 1);						
				}
				
				if(voterMbti.charAt(1)=='S'){
					mbattleResult.setS(mbattleResult.getS() + 1);						
				} else if(voterMbti.charAt(1)=='N') {
					mbattleResult.setN(mbattleResult.getN() + 1);						
				}

				if(voterMbti.charAt(2)=='T'){
					mbattleResult.setT(mbattleResult.getT() + 1);						
				} else if(voterMbti.charAt(2)=='F') {
					mbattleResult.setI(mbattleResult.getI() + 1);						
				}
				
				if(voterMbti.charAt(3)=='J'){
					mbattleResult.setJ(mbattleResult.getJ() + 1);						
				} else if(voterMbti.charAt(3)=='P') {
					mbattleResult.setP(mbattleResult.getP() + 1);						
				}
			}
			
			// mbattle voteCnt 컬럼 1 증가
			Mbattle mbattle = mbattleService.selectMbattleByNo(voter.getMbattleNo());
			mbattle.setVoteCnt(mbattle.getVoteCnt() + 1);
			mbattleService.insertMbattle(mbattle);
			
			// mbattleResult 테이블에 결과 삽입 (업데이트)
			mbattleService.insertMbattleResult(mbattleResult);
			
			// 투표 후, 투표 결과 반환
//			Map<String, Object> res = new HashMap<>();
//			res.put("mbattleResult", mbattleResult);

			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	//특정유저의 mbattleList
	@GetMapping("/mbattlelistbyuser")
	public ResponseEntity<Object> mabttleListByUser(@RequestParam String username,@RequestParam(required = false) Integer page){
		Map<String, Object> res = new HashMap<>();
		PageInfo pageInfo = PageInfo.builder()
				.curPage(page == null ? 1 : page).build();
		try {
			List<Mbattle> mbattleList = mbattleService.findByWriterIdAndPage(username,pageInfo);
			res.put("mbattleList", mbattleList);
			res.put("pageInfo", pageInfo);
		return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}	
	}
	
	//checkList로 mbattle삭제
	@DeleteMapping("/deletembattlelist")
	public ResponseEntity<Object> deleteMbattleList(@RequestParam String sendArrayItems){
		System.out.println("noList 삭제 컨트롤러 진입");
		System.out.println(sendArrayItems);
		//숫자 형태의 리스트로 변환
		List<Integer> noList = Arrays.stream(sendArrayItems.split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		
		try {
			mbattleService.deleteMbattleListByNoList(noList);
			return new ResponseEntity<Object>("삭제 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
}
