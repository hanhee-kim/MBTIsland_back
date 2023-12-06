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
import com.kosta.mbtisland.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/duplicate/{username}")
	public ResponseEntity<String> duplicate(@PathVariable String username) {
		System.out.println("중복체크 컨트롤러 진입"+username);
		try {
			UserEntity user = userService.getUserByUsername(username);
			if(user == null) {
				System.out.println("사용가능");
				return new ResponseEntity<String>("사용가능",HttpStatus.OK);
			}else {
				System.out.println("사용불가능");
				return new ResponseEntity<String>("사용불가",HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("에러로 빠짐");
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/join")
	public String join(@RequestBody UserEntity user) {
		user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
		try {
			userService.joinUser(user);
			return "정상회원가입";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	

}
