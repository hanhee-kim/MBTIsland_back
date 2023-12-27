package com.kosta.mbtisland.dto;

import java.sql.Timestamp;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kosta.mbtisland.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AnswerDto {
	
	private Integer answerNo;
	private String title;
	private String content;
	private Timestamp writeDate;
	private String writerId;
	private Integer questionNo;
	private String fileIdxs;
	
	// 문의글 작성자
	private String questionWriterId;
	
}
