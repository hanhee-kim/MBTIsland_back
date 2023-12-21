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
public class Report {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer no;
	
	@Column
	private String reportType;
	
	@Column
	private String tableType;
	
	@Column
	private Integer reportedPostNo;
	
	@Column
	private Integer reportedCommentNo;
	
	@Column
	private String reportedId;
	
	@Column
	private String reportedTitle;
	
	@Column
	private String reportedContent;
	
	@Column
	private String fileIdxs;
	
	@Column
	private String reporterId;
	
	@Column
	@CreationTimestamp
	private Timestamp reportDate;
	
	@Column
	private String reportReason;
	
	@Column
	private String isCompleted;
	
	@Column
	private String isWarned;
}
