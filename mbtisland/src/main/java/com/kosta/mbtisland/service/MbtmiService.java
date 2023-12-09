package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.entity.Mbtmi;

public interface MbtmiService {
	
	// 주간인기글 목록
	List<Mbtmi> weeklyHotMbtmiList() throws Exception;

}
