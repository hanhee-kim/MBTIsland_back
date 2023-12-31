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
	private String fileType;
	@Column
	private Long fileSize; // 타입 Integer->Long 변경
	@Column
	@CreationTimestamp
	private Timestamp uploadDate;
	@Column
	private Integer postNo;
	@Column
	private String boardType;

}
