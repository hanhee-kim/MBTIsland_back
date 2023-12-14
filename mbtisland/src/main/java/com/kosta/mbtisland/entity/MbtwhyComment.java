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
public class MbtwhyComment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer commentNo;

	@Column
	private String commentContent;
	
	@Column
	private Integer mbtwhyNo;
	
	@Column
	private String isBlocked;
	
	@Column
	private Integer parentcommentNo;
	
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
