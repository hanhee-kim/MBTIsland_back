package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbattleVoter;

public interface MbattleVoterRepository extends JpaRepository<MbattleVoter, Integer> {
	MbattleVoter findMbattleVoterByVoterIdAndMbattleNo(String voterId, Integer mbattleNo);
}
