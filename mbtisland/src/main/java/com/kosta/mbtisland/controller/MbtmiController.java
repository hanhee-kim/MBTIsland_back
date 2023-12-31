package com.kosta.mbtisland.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.mbtisland.dto.MbtmiDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Ban;
import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.FileVo;
import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.kosta.mbtisland.entity.Recommend;
import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.BanRepository;
import com.kosta.mbtisland.repository.FileVoRepository;
import com.kosta.mbtisland.repository.MbtmiCommentRepository;
import com.kosta.mbtisland.service.AlarmService;
import com.kosta.mbtisland.service.BookmarkService;
import com.kosta.mbtisland.service.FileVoService;
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
	@Autowired
	private FileVoRepository fileVoRepository;
	@Autowired
	private FileVoService fileVoService;
	@Autowired
	private BanRepository banRepository;
	
	// 파일 업로드 경로
	@Value("${upload.path}") // org.springframework.beans.factory.annotation.Value
	private String uploadPath;
	
	
	
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
	
	//리스트삭제
	@DeleteMapping("/deletembtmilist")
	public ResponseEntity<Object> deleteMbtmiList(@RequestParam String sendArrayItems){
		System.out.println("noList 삭제 컨트롤러 진입");
		System.out.println(sendArrayItems);
		//숫자 형태의 리스트로 변환
		List<Integer> noList = Arrays.stream(sendArrayItems.split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		
		try {
			mbtmiService.deleteMbtmiList(noList);
			return new ResponseEntity<Object>("삭제 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
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
		
		
//		System.out.println("=======댓글등록 컨트롤러에서 출력=======");
//		System.out.println("sendUser: " + sendUser);
//		System.out.println("no(게시글번호): " + no);
//		System.out.println("comment(댓글내용): " + comment);
//		System.out.println("1차댓글번호: " + parentcommentNo);
//		System.out.println("commentPage: " + commentpage);

		try {
			// 1. 댓글 삽입
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			Timestamp writeDate2 = new Timestamp(new Date().getTime()); // java.util.Date
//			System.out.println("mbtmi컨트롤러의댓글작성메서드에서출력 writeDate: " + writeDate + ", writeDate2: " + writeDate2); 
			// writeDate: 2023-12-28 00:00:00.0, writeDate2: 2023-12-28 03:16:51.288
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

//				Integer alarmCnt = mbtmiService.mbtmiCommentCnt(no); // alarmCnt컬럼값(수정전): 게시글이 가진 댓글수
				Integer alarmCnt = 0; // alarmCnt컬럼값: 기존알림데이터가 있다면 알림cnt+1 없다면 1
				if(alarmForPostWriter!=null) alarmCnt = alarmForPostWriter.getAlarmCnt()+1;
				else alarmCnt = 1;
				String username = mbtmiService.mbtmiDetail(no).getWriterId(); // 알림의 주인==게시글작성자
//				System.out.println("알림카운트: " + alarmCnt);
				
				// 알림 처리 제외 대상에 해당하는지 여부(게시글작성자 본인의 댓글인지 여부)
				Boolean isWrittenByOneSelf = username.equals(sendUser.getUsername());

				// 알림의 존재여부에 따라 alarmCnt컬럼값,읽음여부,읽은일시,알림업데이트일시 업데이트 수행* or 알림데이터 인서트 수행**
				if(alarmForPostWriter!=null && !isWrittenByOneSelf) {
					alarmForPostWriter.setAlarmCnt(alarmCnt);
					alarmForPostWriter.setAlarmIsRead("N");
					alarmForPostWriter.setAlarmReadDate(null);
					alarmForPostWriter.setAlarmUpdateDate(writeDate2);
					alarmService.addAlarm(alarmForPostWriter); // *
				} else if(alarmForPostWriter==null && !isWrittenByOneSelf) {
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
				
//				Integer alarmCnt1 = mbtmiService.mbtmiChildCommentCnt(parentcommentNo); // 알림Cnt1(수정전): 1차댓글이 가진 2차댓글 수
				Integer alarmCnt1 = 0; // 알림Cnt1: 기존알림데이터가 있다면 알림cnt+1 없다면 1
				if(alarmForParentcommentWriter!=null) alarmCnt1 = alarmForParentcommentWriter.getAlarmCnt()+1;
				else alarmCnt1 = 1;
				String username1 = mbtmiCommentRepository.findById(parentcommentNo).get().getWriterId(); // 알림의 주인1==1차댓글의 작성자
//				Integer alarmCnt2 = mbtmiService.mbtmiCommentCnt(no); // 알림Cnt2(수정전): 게시글이 가진 댓글수
				Integer alarmCnt2 = 0;
				if(alarmForPostWriter!=null) alarmCnt2 = alarmForPostWriter.getAlarmCnt()+1;
				else alarmCnt2 = 1;
				String username2 = mbtmiService.mbtmiDetail(no).getWriterId(); // 알림의 주인2==게시글작성자
				
				// 알림 처리 제외 대상에 해당하는지 여부(게시글작성자 본인의 2차댓글인지, 1차댓글작성자 본인의 2차댓글인지 여부)
				Boolean isWrittenByParentcommentWriter = username1.equals(sendUser.getUsername());
				Boolean isWrittenByPostWriter = username2.equals(sendUser.getUsername());
				
				// 2-2-1. 1차댓글 작성자를 향한 알림데이터 업데이트 또는 인서트
				if(alarmForParentcommentWriter!=null && !isWrittenByParentcommentWriter) {
					alarmForParentcommentWriter.setAlarmCnt(alarmCnt1);
					alarmForParentcommentWriter.setAlarmUpdateDate(writeDate2);
					alarmForParentcommentWriter.setAlarmIsRead("N");
					alarmForParentcommentWriter.setAlarmReadDate(null);
					alarmService.addAlarm(alarmForParentcommentWriter); // alarmCnt컬럼값과 읽음여부, 읽은일시, 알림업데이트일시 컬럼값 업데이트 수행
				} else if(alarmForParentcommentWriter==null && !isWrittenByParentcommentWriter) {
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
				
				// 2-2-2. 게시글 작성자를 향한 알림데이터 업데이트
				if(alarmForPostWriter!=null && !isWrittenByPostWriter) {
					alarmForPostWriter.setAlarmCnt(alarmCnt2);
					alarmForPostWriter.setAlarmUpdateDate(writeDate2);
					alarmService.addAlarm(alarmForPostWriter); // alarmCnt컬럼값만 업데이트 수행
				}
			}
			
			
			PageInfo pageInfo = PageInfo.builder().curPage(commentpage==null? 1: commentpage).build();
			List<MbtmiComment> mbtmiCommentList = mbtmiService.mbtmiCommentListByMbtmiNo(no, pageInfo);
			Integer mbtmiCommentCnt = mbtmiService.mbtmiCommentCnt(no);
			
			// 삽입된 댓글
			Integer writtenCommentNo = mbtmiComment.getCommentNo(); // 방금 삽입된 댓글의 pk
			
			Map<String, Object> res = new HashMap<>();
			res.put("pageInfo", pageInfo);
			res.put("mbtmiCommentList", mbtmiCommentList);
			res.put("mbtmiCommentCnt", mbtmiCommentCnt);
			res.put("writtenCommentNo", writtenCommentNo);
			
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	// 게시글 등록--->대체
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
	
	
	// 이미지 없이 게시글 삽입
	@PostMapping("/mbtmiwritewithoutimages")
	public ResponseEntity<Object> quillTest(@RequestBody MbtmiDto mbtmiDto) {
		System.out.println("quillTest가 받은 mbtmiDto: " + mbtmiDto); // title, category, writerId등, content
		System.out.println("*quillTest가 받은 mbtmiDto의 content값: " + mbtmiDto.getContent());
		
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
	
	// 프런트의 uploadImage함수 안에서 호출되며 에디터에 삽입된 이미지를 디렉토리에 업로드, 파일테이블에 인서트하고 pk를 리턴
	@PostMapping("/uploadImage")
	public ResponseEntity<Object> handleImageUpload(@RequestParam("image") MultipartFile file, @RequestParam("postNo") Integer postNo) {
		
		System.out.println("/uploadImage메서드 호출됨");
		
		try {
			// 파일 파라미터가 있는지 체크
			if (file.isEmpty()) {
                return new ResponseEntity<>("업로드될 파일이 없음", HttpStatus.BAD_REQUEST);
            }
			
			// 원하는 디렉토리에 업로드
			String fileName = file.getOriginalFilename();
			
			FileVo fileVo = new FileVo();
			fileVo.setFilePath(uploadPath);
			fileVo.setFileName(fileName);
			fileVo.setFileSize(file.getSize());
			fileVo.setFileType(file.getContentType());
			LocalDate currentDate = LocalDate.now();
			Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
			fileVo.setUploadDate(writeDate);
			fileVo.setPostNo(postNo);
			fileVo.setBoardType("mbtmi");
			
			// 파일테이블에 인서트 먼저 수행
			fileVoRepository.save(fileVo);
			// upload폴더에 저장
			File uploadFile = new File(uploadPath + fileVo.getFileIdx());
			file.transferTo(uploadFile);
			
			// 파일테이블에 자동생성된 idx값 반환
			Integer fileIdx = fileVo.getFileIdx();
			System.out.println("fileIdx: " + fileIdx); // 출력됨
            
            return new ResponseEntity<>(fileIdx, HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 게시글의 fileIdx컬럼 업데이트
	@PostMapping("/updateFileIdxs/{postNo}")
	public ResponseEntity<?> updateFileIdxs(@PathVariable("postNo") Integer postNo, @RequestBody Map<String, Object> payload) {
	    try {
	        // fileIdx 추출
	        Integer fileIdx = Integer.valueOf(payload.get("fileIdx").toString());
	        System.out.println("fileIdx: " + fileIdx);

	        Mbtmi mbtmi = mbtmiService.updateFileIdxs(postNo, fileIdx); // 게시글의 파일 컬럼 업데이트
	        
	        return new ResponseEntity<>(mbtmi, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	// 게시글 content에 <img src=fileIdx>가 포함되도록 업데이트
	@PostMapping("/mbtmiContainingImgTags/{postNo}")
	public ResponseEntity<Object> containImgTags(@PathVariable("postNo") Integer postNo, @RequestBody MbtmiDto mbtmiDto) {
		System.out.println("postNo: " + postNo);
		System.out.println("mbtmiDto: " + mbtmiDto);
		
		try {
			mbtmiDto.setNo(postNo);
			Mbtmi mbtmi = mbtmiService.updateContainingFileIdxs(mbtmiDto);
			return new ResponseEntity<>(mbtmi, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 이미지 출력(게시글 상세, 게시글 수정폼)
	@GetMapping("/mbtmiimg/{fileIdx}")
	public void imageView(@PathVariable Integer fileIdx, HttpServletResponse response) {
		System.out.println("imageView메서드 호출");
		try {
			fileVoService.readImage(fileIdx, response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// * 메인페이지 로그인한 활동정지 회원의 정지기간 조회
	@GetMapping("/usersbanperiod/{username}")
	public ResponseEntity<Object> usersBanPeriod(@PathVariable String username) {
//		System.out.println("usersBanPeriod메서드 호출! usename: " + username);
		try {
			Ban ban = banRepository.findByUsername(username);
			return new ResponseEntity<>(ban, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
