package com.kosta.mbtisland.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInfo {
	
	private Integer allPage;
	private Integer curPage;
	private Integer startPage;
	private Integer endPage;

	public PageInfo() {}
	public PageInfo(Integer page) {
		super();
		this.curPage = page;
	}
	public PageInfo(Integer allPage,Integer curPage,Integer startPage,Integer endPage) {
		this.allPage = allPage;
		this.curPage = curPage;
		this.startPage = startPage;
		this.endPage = endPage;
	}

}
