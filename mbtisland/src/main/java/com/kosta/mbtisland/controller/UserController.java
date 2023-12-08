package com.kosta.mbtisland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;
import com.kosta.mbtisland.service.EmailService;
import com.kosta.mbtisland.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final EmailService emailService;
	
	//ID중복체크
	@GetMapping("/duplicate/{username}")
	public ResponseEntity<Object> duplicate(@PathVariable String username) {
		System.out.println("중복체크 컨트롤러 진입"+username);
		try {
			UserEntity user = userService.getUserByUsername(username);
			if(user == null) {
				System.out.println("사용가능");
				return new ResponseEntity<Object>("사용가능",HttpStatus.OK);
			}
			else {
				System.out.println("사용불가능");
				return new ResponseEntity<Object>("사용불가",HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("에러로 빠짐");
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/sendmail/{userEmail}")
	public ResponseEntity<Object> sendmail(@PathVariable String userEmail){
		try {
			UserEntity user = userService.getUserByUserEmail(userEmail);
			if(user == null) {
				//이메일보내고 코드 받아서 날리는 작업
				String ePw = emailService.sendSimpleMessage(userEmail);
				return new ResponseEntity<Object>(ePw,HttpStatus.OK);
			}else {
				//email중복
				return new ResponseEntity<Object>("email중복",HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/123")
	public String join(@RequestBody UserEntity sendUser) {
		System.out.println("join controller진입");
		sendUser.setUserPassword(bCryptPasswordEncoder.encode(sendUser.getUserPassword()));
		try {
//			System.out.println("join controller진입");
			System.out.println(sendUser.getUsername());
			userService.joinUser(sendUser);
			
			return "정상회원가입";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	

}
