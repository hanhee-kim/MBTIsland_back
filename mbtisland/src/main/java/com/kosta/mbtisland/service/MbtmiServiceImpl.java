package com.kosta.mbtisland.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.repository.MbtmiDslRepository;
import com.kosta.mbtisland.repository.MbtmiRepository;

@Service
public class MbtmiServiceImpl implements MbtmiService {
	
	@Autowired
	private MbtmiRepository mbtmiRepository;
	@Autowired
	private MbtmiDslRepository mbtmiDslRepository;

	
	// 주간인기글 목록
	@Override
	public List<Mbtmi> weeklyHotMbtmiList() throws Exception {
		List<Mbtmi> weeklyHotMbtmiList = mbtmiDslRepository.findWeeklyHotMbtmiListByCategoryAndRecommendCnt();
		if(weeklyHotMbtmiList.size()==0) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
		return weeklyHotMbtmiList;
	}

}
