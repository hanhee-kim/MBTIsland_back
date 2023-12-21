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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoticeDto {

	private Integer no;
	private String title;
	private String content;
	private Integer viewCnt;
	private Timestamp writeDate;
	private String writerId;
	private String isHidden;
	private String fileIdxs;
	
}
