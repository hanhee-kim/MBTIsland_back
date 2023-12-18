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
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer noteNo;
	@Column
	private String sentUsername;
	@Column
	private String noteContent;
	@Column
	private String receiveUsername;
	@Column
	@CreationTimestamp
	private Timestamp sentDate;
	@Column
	@ColumnDefault("N")
	private String noteIsRead;

}
