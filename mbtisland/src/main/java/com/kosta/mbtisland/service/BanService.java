package com.kosta.mbtisland.service;

import com.kosta.mbtisland.entity.Ban;

public interface BanService {
	public Ban selectBanByUsername(String username) throws Exception; // 밴 조회 (유저명)
	public void insertBan(Ban ban) throws Exception; // 밴 삽입
}
