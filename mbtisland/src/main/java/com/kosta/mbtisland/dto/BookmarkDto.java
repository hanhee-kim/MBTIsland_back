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
public class BookmarkDto {
	private Integer no;
	private String username;
	private Integer postNo;
	private String boardType;
	private String boardTitle;
	private Timestamp writeDate;
	private Integer commentCnt;
	
	
	
	
	
	
	
	
	
}
