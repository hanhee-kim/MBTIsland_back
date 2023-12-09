package com.kosta.mbtisland.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@ToString
public class Mbtmi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	@Column
	private String title;
	@Column
	private String content;
	@Column
	private String category;
	
	@Column
	@ColumnDefault("0")
	private Integer viewCnt;
	@Column
	@ColumnDefault("0")
	private Integer recommendCnt;
	
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
	private String fileIdxs;
	
	
	
	
	
	

}
