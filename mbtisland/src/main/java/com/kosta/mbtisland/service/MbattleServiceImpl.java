package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.repository.MbattleRepository;

@Service
public class MbattleServiceImpl implements MbattleService {
	@Autowired
	private MbattleRepository mbattleRepository;
	
	// 게시글 작성
	@Override
	public Integer insertMbattle(Mbattle mbattle) throws Exception {
		return mbattleRepository.save(mbattle).getNo();
	}
}
