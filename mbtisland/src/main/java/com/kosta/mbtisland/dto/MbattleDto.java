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
public class MbattleDto {
	
	private Integer no;
	private String voteItem1;
	private String voteItem2;
	private Integer viewCnt;
	private Integer voteCnt;
	private Timestamp writeDate;
	private String isBlocked;
	private String writerId;
	private String writerNickname;
	private String writerMbti;
	private String writerMbtiColor;
	private String title;
	private String fileIdx1;
	private String fileIdx2;
	
	// 게시글이 가지는 댓글 수
	private Integer commentCnt;
}
