package com.kosta.mbtisland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.dto.NoteDto;
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
}
