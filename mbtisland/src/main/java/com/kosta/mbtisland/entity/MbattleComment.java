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
import org.springframework.data.relational.core.mapping.Table;

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
public class MbattleComment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer commentNo;
	
	@Column
	private String commentContent;
	
	@Column
	private Integer mbattleNo;
	
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
	@CreationTimestamp
	private Timestamp writeDate;
	
	@Column
	private String isRemoved;
}
