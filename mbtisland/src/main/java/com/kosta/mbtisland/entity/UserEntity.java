package com.kosta.mbtisland.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class UserEntity {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userIdx;
	@Column(unique = true)
	private String username;
	private String userPassword;
	private String userNickname;
	private String userMbti;
	private String userMbtiColor;
//	@CreationTimestamp
//	@UpdateTimestamp
	private Date userMbtiChangeDate;
	private String userEmail;
	private String userRole;
	private Integer userWarningCnt;
	private Integer userBangCnt;
	private String isLeave;
	private String isBanned;
	@CreationTimestamp
	private Date joinDate;
	private Date leaveDate;
	private Integer visitCnt;
	//OAuth2 필드
	private String provider;
	private String providerId;
	

//	//유저 권한이 여러개일때?
//	public List<String> getRoleList() {
//		if(this.userRole.length()>0) {
//			return Arrays.asList(this.userRole.split(","));
//		}
//		return new ArrayList<>();
//	}
//	
	
	
	
	
	
	
	
	
	
	

}
