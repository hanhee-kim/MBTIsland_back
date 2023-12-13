package com.kosta.mbtisland.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Note;
import com.kosta.mbtisland.service.EmailService;
import com.kosta.mbtisland.service.NoteService;
import com.kosta.mbtisland.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoteController {
	private final UserService userService;
	private final NoteService noteService;
	
	@PostMapping("/notewrite")
	public ResponseEntity<Object> noteWrite(@RequestBody NoteDto noteDto){
		System.out.println("noteWrite Controller진입");
		System.out.println(noteDto.getSentUsername());
		try {
			noteService.noteWrite(noteDto);
			return new ResponseEntity<Object>("note등록성공",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/notelistofuser")
	public ResponseEntity<Object> noteListOfUser(@RequestParam(required = false) String username,@RequestParam(required = false)String noteType,@RequestParam(required = false)String readType,@RequestParam(required = false)Integer page){
		System.out.println("노트 불러오기 컨트롤러");
		System.out.println("user:"+username+" noteType:"+noteType+" readType:"+readType+" page:"+page);
		PageInfo pageInfo = PageInfo.builder().curPage(page==null? 1: page).build();
		Map<String, Object> res = new HashMap<>();
		try {
			List<NoteDto> noteList = noteService.getNoteListByUsernameAndNoteTypeAndReadTypeAndPage(username,noteType,readType,pageInfo);
			res.put("noteList", noteList);
			res.put("pageInfo", pageInfo);
//			NoteDto n = noteList.get(0);
//			System.out.println("첫번째 내용"+n.getNoteContent());
			return new ResponseEntity<Object>(res,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
	}
	//notedetail
	@GetMapping("/notedetail/{noteNo}")
	public ResponseEntity<Object> noteDetail(@PathVariable Integer noteNo){
		System.out.println("노트 자세히 컨트롤러");
		try {
			NoteDto note = noteService.getNoteDtoByNoteNo(noteNo);
			return new ResponseEntity<Object>(note,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
}
