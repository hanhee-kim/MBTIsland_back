package com.kosta.mbtisland.dto;

import java.sql.Timestamp;

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
public class MbtwhyDto {

	private Integer no;
	private String content;
	private String mbtiCategory;
	private Integer viewCnt;
	private Integer recommendCnt;
	private Timestamp writeDate;
	private String isBlocked;
	private String writerId;
	private String writerNickname;
	private String writerMbti;
	private String writerMbtiColor;
	
	// 게시글이 가지는 댓글 수
	private Integer commentCnt;
}
