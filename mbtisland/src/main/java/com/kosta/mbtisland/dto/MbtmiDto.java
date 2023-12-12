package com.kosta.mbtisland.dto;

import java.sql.Timestamp;

import com.kosta.mbtisland.entity.Mbtmi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MbtmiDto {
	
	private Integer no;
	private String title;
	private String content;
	private String category;
	private Integer viewCnt;
	private Integer recommendCnt;
	private Timestamp writeDate;
	private String isBlocked;
	private String writerId;
	private String writerNickname;
	private String writerMbti;
	private String writerMbtiColor;
	private String fileIdxs;
	
	// 게시글이 가진 댓글수
	private Integer commentCnt;
	


}
