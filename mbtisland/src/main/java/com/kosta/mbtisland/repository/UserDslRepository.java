package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.QMbtwhy;
import com.kosta.mbtisland.entity.QUserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	//mbtwhyRepository에서 가져오면대는데..?
	public List<Mbtwhy> findMbtwhyListByUsername(String username) throws Exception{
		QUserEntity user = QUserEntity.userEntity;
		QMbtwhy mbtwhy = QMbtwhy.mbtwhy;
		
		List<Mbtwhy> mbtwhyList = jpaQueryFactory.select(mbtwhy)
				.from(mbtwhy)
				.join(user)
				.on(user.username.eq(mbtwhy.writerId))
				.where(mbtwhy.writerId.eq(username))
				.fetch();
		return mbtwhyList;
	}
}
