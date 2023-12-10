package com.kosta.mbtisland.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Ban {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer banIdx;
	
	@Column
	private String username;
	
	@Column
	@CreationTimestamp
	private Timestamp banStartDate;
	
	@Column
	private Timestamp banEndDate;
}
