package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleVoter;
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
	
	// 게시글 조회 (게시글 번호, 댓글 페이지, 유저 아이디)
		@GetMapping("/mbattledetail")
		public ResponseEntity<Object> mbattleDetail(@RequestParam(required = false) Integer no, @RequestParam(required = false) String username) {
			try {
				
				// 조회수 증가
				mbattleService.increaseViewCount(no);
				// Mbtwhy 게시글 (추천 수 포함하므로, 게시글 처음 보여질 때는 해당 GetMapping에서 추천수 가져와서 사용)
				Mbattle mbattle = mbattleService.selectMbattleByNo(no);
				// 투표 데이터 조회 (투표 여부)
				MbattleVoter mbattleVoter = mbattleService.selectIsVoteByUsernameAndPostNo(username, no);
				// 북마크 여부 조회
				Boolean isMbattleBookmarked = bookmarkService.selectIsBookmarkByUsernameAndPostNoAndBoardType(username, no, "mbattle");
				
				Map<String, Object> res = new HashMap<>();
				res.put("mbattle", mbattle);
				res.put("mbattleVoter", mbattleVoter);
				res.put("isMbattleBookmarked", isMbattleBookmarked);
				
				return new ResponseEntity<Object>(res, HttpStatus.OK);
			} catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
	
	// 이미지 출력
	@GetMapping("/mbattleimg/{fileIdx}")
	public void imageView(@PathVariable Integer fileIdx, HttpServletResponse response) {
		try {
			fileVoService.readImage(fileIdx, response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
