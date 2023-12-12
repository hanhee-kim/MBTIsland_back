package com.kosta.mbtisland.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class NoteDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
	
	//

}
