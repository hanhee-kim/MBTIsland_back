package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.QMbtwhy;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbtwhyDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지)
	public List<Mbtwhy> findMbtwhyListByMbtiCategoryAndPageAndSearchTypeAndSearchValueAndSortType
		(String mbtiCategory, PageRequest pageRequest, String searchType, String searchValue, String sortType) throws Exception {
		QMbtwhy qmbtwhy = QMbtwhy.mbtwhy;
		
		return jpaQueryFactory.selectFrom(qmbtwhy)
						.orderBy(qmbtwhy.no.desc())
						.offset(pageRequest.getOffset())
						.where(qmbtwhy.mbtiCategory.eq(mbtiCategory)).fetch();
		
//		return jpaQueryFactory.selectFrom(qmbtwhy)
//				.orderBy(qmbtwhy.no.desc())
//				.offset(pageRequest.getOffset())
//				.where(searchType!=null && searchValue!=null && sortType!=null ? qmbtwhy.mbtiCategory.eq(mbtiCategory) : null).fetch();
	}
	
	// 게시글 개수 조회 (MBTI 타입)
	public Long findMbtwhyCntByMbtiCategory(String mbtiCategory) throws Exception {
		QMbtwhy qmbtwhy = QMbtwhy.mbtwhy;
		
		return jpaQueryFactory.select(qmbtwhy.count())
						.from(qmbtwhy)
						.where(qmbtwhy.mbtiCategory.eq(mbtiCategory)).fetchOne();
	}
	
	// 게시글 조회 (게시글 번호)
	public Mbtwhy findMbtwhyByNo(Integer no) throws Exception {
		QMbtwhy qmbtwhy = QMbtwhy.mbtwhy;
		
		return jpaQueryFactory.selectFrom(qmbtwhy)
						.where(qmbtwhy.no.eq(no)).fetchOne();
	}
	
}
