package com.kosta.mbtisland.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.QMbtwhy;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbtwhyDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 글 번호로 게시글 조회
	public Mbtwhy findMbtyByNo(Integer no) throws Exception {
		QMbtwhy qmbtwhy = QMbtwhy.mbtwhy;
		
		return null;
	}
}
