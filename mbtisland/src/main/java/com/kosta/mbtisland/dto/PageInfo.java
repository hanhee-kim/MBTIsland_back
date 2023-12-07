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

}
