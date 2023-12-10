package com.kosta.mbtisland.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.UserEntity;
import com.kosta.mbtisland.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 

	private String getMbtiColor(String mbti) {
		String color = "";
		switch (mbti) {
		case "ENFJ":
			color = "#82B8AD";
			break;
		case "ENFP":
			color = "#FFD966";
			break;
		case "ENTJ":
			color = "#35598F";
			break;
		case "ENTP":
			color = "#B6634A";
			break;
		case "ESFJ":
			color = "#E6D0CE";
			break;
		case "ESFP":
			color = "#F0A4AB";
			break;
		case "ESTJ":
			color = "#596D55";
			break;
		case "ESTP":
			color = "#D8927A";
			break;
		case "INFJ":
			color = "#EAEFF9";
			break;
		case "INFP":
			color = "#648181";
			break;
		case "INTJ":
			color = "#D8D4EA";
			break;
		case "INTP":
			color = "#9BB7D4";
			break;
		case "ISFJ":
			color = "#F2DCB3";
			break;
		case "ISFP":
			color = "#BDC9A6";
			break;
		case "ISTJ":
			color = "#ADB1B0";
			break;
		case "ISTP":
			color = "#4D6879";
			break;
		

		default:
			break;
		}
		
		return color;
	}
	
	@Override
	public void joinUser(UserEntity user) throws Exception {
		UserEntity userCheck = userRepository.findByUsername(user.getUsername());
		if(userCheck == null) {
			// userMbtiColor , USER_MBTI_CHANGE_DATE , USER_ROLE  , provider (null로 둘지 노말로 둘지)
			//userMbtiColor셋팅
			user.setUserMbtiColor(getMbtiColor(user.getUserMbti()));
			//changeDate 셋팅
			user.setUserMbtiChangeDate(new Timestamp(new Date().getTime()));
			//userRole 셋ㅌ
			user.setUserRole("ROLE_USER");
			userRepository.save(user);
		}else {
			throw new Exception("아이디 중복");
		}
	}
	

	@Override
	public UserEntity getUserByUserIdx(Integer userIdx) throws Exception {
		Optional<UserEntity> ouser = userRepository.findById(userIdx);
		if(ouser.isPresent()) {
			return ouser.get();
		}else {
			throw new Exception("유저번호 없음");
		}
	}

	@Override
	public UserEntity getUserByUsername(String username) throws Exception {
		UserEntity user = userRepository.findByUsername(username);
		if(user != null) {
			return user;
		}else {
			return null;
//			throw new Exception("username 없음");
		}
	}

	@Override
	public UserEntity getUserByUserEmailAndProviderNull(String userEmail) throws Exception {
		UserEntity user = userRepository.findByUserEmailAndProviderIsNull(userEmail);
		if(user != null) {
			return user;
		}else {
			return null;
		}
	}

	@Override
	public void modifyUser(UserEntity user) throws Exception {
		userRepository.save(user);	
	}

	@Override
	public UserEntity setAddUser(UserEntity user, String mbti) throws Exception {
		Optional<UserEntity> oUser = userRepository.findById(user.getUserIdx());
		if(oUser.isPresent()) {
			UserEntity modifyUser = oUser.get();
			modifyUser.setUserMbti(mbti);
			modifyUser.setUserMbtiColor(getMbtiColor(mbti));
			modifyUser.setUserMbtiChangeDate(new Timestamp(new Date().getTime()));
			modifyUser.setUserRole("ROLE_USER");
			userRepository.save(modifyUser);
			return modifyUser;
		}else {
			throw new Exception("해당 유저 없음");
		}
		
	}

	@Override
	public UserEntity modifyUser(UserEntity user, Map<String, Object> param) throws Exception {
		if(user != null) {
			String userNickname = (String)param.get("userNickname");
			String userMbti = (String)param.get("userMbti");
			String beforeMbti = (String)param.get("beforeMbti");
			if(user.getProvider() == "" || user.getProvider() == null) {
				String userEmail = (String)param.get("userEmail");
				String userPassword = (String)param.get("userPassword");
				user.setUserEmail(userEmail);
				user.setUserPassword(bCryptPasswordEncoder.encode(userPassword));
			}
			user.setUserNickname(userNickname);
			if(!userMbti.equals(beforeMbti)) {
				user.setUserMbti(userMbti);
				user.setUserMbtiColor(getMbtiColor(userMbti));
				user.setUserMbtiChangeDate(new Timestamp(new Date().getTime()));
			}
			userRepository.save(user);
			return user;
		}else {
			throw new Exception("해당 유저 없음");
		}
	}

}
