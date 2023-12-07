package com.kosta.mbtisland.entity;

import java.sql.Date;
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

@Entity(name="FILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class FileVo {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer fileIdx;
	@Column
	private String filePath;
	@Column
	private String fileName;
	@Column
	private String type;
	@Column
	private Integer fileSize;
	@Column
	@CreationTimestamp
	private Timestamp uploadDate;
	@Column
	private Integer postNo;
	@Column
	private String boardType;

}
