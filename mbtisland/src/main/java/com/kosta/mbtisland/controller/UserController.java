package com.kosta.mbtisland.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.mbtisland.config.auth.PrincipalDetails;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Mbtwhy;
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
	
	//가입시 메일 보내기 누를때
	@GetMapping("/sendmail/{userEmail}")
	public ResponseEntity<Object> sendmail(@PathVariable String userEmail){
		try {
			UserEntity user = userService.getUserByUserEmailAndProviderNull(userEmail);
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
	
	//가입할때
	@PostMapping("/join")
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
	
	//main 로드시에 실행
	@GetMapping("/user")
	public ResponseEntity<UserEntity> user(Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println(principalDetails.getUser().getUserIdx());
		System.out.println(principalDetails.getUser().getUsername());
		System.out.println(principalDetails.getUser().getUserPassword());
		System.out.println(principalDetails.getUser().getUserRole());
		return new ResponseEntity<UserEntity>(principalDetails.getUser(), HttpStatus.OK);
		
	}
	
	//마이페이지에서 유저정보 수정
	@PostMapping("/user/modify")
	public ResponseEntity<Object> userModify(Authentication authentication,@RequestBody Map<String,Object> param){
		System.out.println("user/modify 진입");
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		try {
			UserEntity user = userService.modifyUser(principalDetails.getUser(), param);
			System.out.println("비번"+principalDetails.getUser().getUserPassword());
			return new ResponseEntity<Object>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@GetMapping("/guest/{userMbti}")
	public ResponseEntity<Object> guest(Authentication authentication,@PathVariable String userMbti) {
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println(principalDetails.getUser().getUserIdx());
		try {
			UserEntity user = userService.setAddUser(principalDetails.getUser(), userMbti);
			return new ResponseEntity<Object>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	//아이디찾기
	@PostMapping("/find")
	public ResponseEntity<Object> findIdPassword(@RequestBody Map<String,String> param){
		String email = (String)param.get("userEmail");
		String type = (String)param.get("type");
		System.out.println(email+"   "+type);
		try {
			UserEntity user =  userService.getUserByUserEmailAndProviderNull(email);
			if(user == null) {
				return new ResponseEntity<Object>("해당 Email 존재하지 않음.", HttpStatus.OK);
			}else {
				if(type.equals("ID")) {
					//id찾기
					try {
						System.out.println("id찾기 진입");
						// email,id 이메일보내기
						emailService.sendFindIdMessage(email, user.getUsername());
						return new ResponseEntity<Object>("ID전송완료", HttpStatus.OK);					
					} catch (UnsupportedEncodingException | MessagingException e) {
						e.printStackTrace();
						return new ResponseEntity<Object>("ID전송에러", HttpStatus.BAD_REQUEST);					
					} 
				}else { 
					//password찾기
					try {
						System.out.println("pw찾기 진입");
						System.out.println("원래 비밀번호 ? : "+user.getUserPassword());
						String ePw = emailService.createKey();
						System.out.println(ePw);
						user.setUserPassword(bCryptPasswordEncoder.encode(ePw)); // ePw 비밀번호 암호화해서 user셋팅
						userService.modifyUser(user); //modify
						System.out.println("셋팅한 비밀번호?:"+user.getUserPassword());
						//email,새로 업뎃한 pw 이메일 보내기
						emailService.sendFindPasswordMessage(email, ePw);
						return new ResponseEntity<Object>("PW전송완료", HttpStatus.OK);					
					} catch (UnsupportedEncodingException | MessagingException e) {
						e.printStackTrace();
						return new ResponseEntity<Object>("PW전송에러", HttpStatus.BAD_REQUEST);					
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return new ResponseEntity<Object>("ID,PW찾기 오류",HttpStatus.BAD_REQUEST);
		}		
	}
	

	

}
