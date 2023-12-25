package com.kosta.mbtisland.dto;


import java.sql.Timestamp;

import javax.persistence.Column;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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
public class AlarmDto {
	private Integer alarmNo;
	private String username;
	private String alarmType;
	private String detailType;
	private Integer detailNo;
	private String alarmContent;
	private Integer alarmTargetNo;
	private String alarmTargetFrom;
	private String alarmIsRead;
	private Timestamp alarmReadDate;
	private Timestamp alarmUpdateDate;
	private Integer alarmCnt;
}
