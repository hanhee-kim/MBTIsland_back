package com.kosta.mbtisland.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Mbattle {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer no;
	
	@Column
	private String voteItem1;
	
	@Column
	private String voteItem2;
	
	@Column
	private Integer viewCnt;
	
	@Column
	private Integer voteCnt;
	
	@Column
	@CreationTimestamp
	private Timestamp writeDate;
	
	@Column
	private String isBlocked;
	
	@Column
	private String writerId;
	
	@Column
	private String writerNickname;
	
	@Column
	private String writerMbti;
	
	@Column
	private String writerMbtiColor;
	
	@Column
	private String title;
	
	@Column
	private Integer file1Idx;
	
	@Column
	private Integer file2Idx;
}
