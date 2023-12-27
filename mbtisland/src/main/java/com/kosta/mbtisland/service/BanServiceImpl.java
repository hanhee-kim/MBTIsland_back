package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Ban;
import com.kosta.mbtisland.repository.BanRepository;

@Service
public class BanServiceImpl implements BanService {
	@Autowired
	BanRepository banRepository;
	
	// 밴 조회 (유저명)
	@Override
	public Ban selectBanByUsername(String username) throws Exception {
		return banRepository.findByUsername(username);
	}

	// 밴 삽입
	@Override
	public void insertBan(Ban ban) throws Exception {
		banRepository.save(ban);
	}
}
