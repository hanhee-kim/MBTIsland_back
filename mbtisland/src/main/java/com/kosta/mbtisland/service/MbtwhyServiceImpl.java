package com.kosta.mbtisland.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.repository.MbtwhyRepository;

@Service
public class MbtwhyServiceImpl implements MbtwhyService {
	
	@Autowired
	private MbtwhyRepository mbtwhyRepository;
//	private final MbtwhyRepository mbtwhyRepository;
	
	// Mbtwhy 게시글 작성 (삽입)
	@Override
	public void insertMbtwhy(Mbtwhy mbtwhy) throws Exception {
		
		mbtwhyRepository.save(mbtwhy);
		
	}
}
