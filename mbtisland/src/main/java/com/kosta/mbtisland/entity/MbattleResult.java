package com.kosta.mbtisland.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class MbattleResult {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer no;
	
	@Column
	private Integer mbattleNo;
	
	@Column
	private Integer voteItem;
	
	@Column
	private Integer E;
	
	@Column
	private Integer I;
	
	@Column
	private Integer N;
	
	@Column
	private Integer S;
	
	@Column
	private Integer F;
	
	@Column
	private Integer T;
	
	@Column
	private Integer J;
	
	@Column
	private Integer P;
}
