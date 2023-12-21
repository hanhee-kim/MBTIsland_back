package com.kosta.mbtisland.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleVoter;
import com.kosta.mbtisland.repository.MbattleRepository;
import com.kosta.mbtisland.repository.MbattleVoterRepository;

@Service
public class MbattleServiceImpl implements MbattleService {
	@Autowired
	private MbattleRepository mbattleRepository;
	
	@Autowired
	private MbattleVoterRepository mbattleVoterRepository;
	
	// Entity 게시글 조회
	@Override
	public Mbattle selectMbattleByNo(Integer no) throws Exception {
		Optional<Mbattle> ombattle = mbattleRepository.findById(no);
		if(ombattle.isEmpty()) throw new Exception(no + "번 게시글이 존재하지 않습니다.");
		Mbattle mbattle = ombattle.get();
		return mbattle;
	}
	
	// 조회수 증가
	@Override
	public void increaseViewCount(Integer no) throws Exception {
		Mbattle mbattle = selectMbattleByNo(no);
		mbattle.setViewCnt(mbattle.getViewCnt()+1);
		mbattleRepository.save(mbattle);
	}
	
	// 게시글 작성
	@Override
	public Integer insertMbattle(Mbattle mbattle) throws Exception {
		return mbattleRepository.save(mbattle).getNo();
	}
	
	// 투표 데이터 조회
	@Override
	public MbattleVoter selectIsVoteByUsernameAndPostNo(String voterId, Integer no) throws Exception {
		return mbattleVoterRepository.findByVoterIdAndMbattleNo(voterId, no);
	}
}
