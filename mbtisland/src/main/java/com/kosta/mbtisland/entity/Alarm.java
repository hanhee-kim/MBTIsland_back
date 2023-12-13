package com.kosta.mbtisland.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@ToString
public class Alarm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer alarmNo;
	@Column
	private String username;
	@Column
	private String alarmType;
	@Column
	private Integer alarmTargetNo;
	@Column
	private String alarmTargetFrom;
	@Column
	@ColumnDefault("N")
	private String alarmIsRead;
	@Column
	private Timestamp alarmReadDate;
	@Column
	@CreationTimestamp
	private Timestamp alarmUpdateDtae;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
