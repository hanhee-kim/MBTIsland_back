package com.kosta.mbtisland.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.kosta.mbtisland.dto.BookmarkDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookmarkController {
	private final BookmarkService bookmarkService;
	
	//user의 bookmarkList가져오기
	@GetMapping("/mybookmarklist/{username}/{page}")
	public ResponseEntity<Map<String,Object>> bookmarkLis(@PathVariable String username,@PathVariable(required = false) Integer page){
		System.out.println("북마크리스트 컨트롤러 진입"+username+" _ "+page);
		PageInfo pageInfo = new PageInfo(page);
		pageInfo.setCurPage(page==null? 1:page);
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			List<BookmarkDto> bookmarkList = bookmarkService.getBookmarkListByUsername(username, pageInfo);
			res.put("bookmarkList", bookmarkList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("errMsg", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(res,HttpStatus.BAD_REQUEST);
		}
	}
	
	//배열에 담긴 no로 북마크 삭제
	@DeleteMapping("/deletebookmark")
	public ResponseEntity<String> deleteBookmark(@RequestParam String sendArrayItems){
		System.out.println("noList 삭제 컨트롤러 진입");
		System.out.println(sendArrayItems);
		//숫자 형태의 리스트로 변환
		List<Integer> noList = Arrays.stream(sendArrayItems.split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		
		try {
			bookmarkService.deleteBookmarkList(noList);
			return new ResponseEntity<String>("삭제 성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.OK);
		}
	}

}
