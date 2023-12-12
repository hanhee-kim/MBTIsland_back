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
public class NoteDto {

	private Integer noteNo;
	private String sentUsername;
	private String sentUserNick;
	private String noteContent;
	private String receiveUsername;
	private String receiveUserNick;
	private Timestamp sentDate;
	private String noteIsRead;
}
