package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbattleResult;

public interface MbattleResultRepository extends JpaRepository<MbattleResult, Integer> {
	MbattleResult findMbattleResultByMbattleNoAndVoteItem(Integer mbattleNo, Integer voteItem) throws Exception;
}
