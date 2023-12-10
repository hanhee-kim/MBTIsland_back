package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtwhy.mbtwhy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbtwhyDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	public List<Mbtwhy> findMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbti, PageRequest pageRequest, String search, String sort) throws Exception {
		
		return jpaQueryFactory.selectFrom(mbtwhy)
				.where(search!=null? mbtwhy.content.containsIgnoreCase(search) : null,
						sort!=null? mbtwhy.content.containsIgnoreCase(sort) : null,
						mbtwhy.isBlocked.eq("N"),
						mbtwhy.mbtiCategory.eq(mbti))
						.orderBy(mbtwhy.no.desc())
						.offset(pageRequest.getOffset()).fetch();
		
//		return jpaQueryFactory.selectFrom(mbtwhy)
//				.orderBy(mbtwhy.no.desc())
//				.offset(pageRequest.getOffset())
//				.where(search!=null && sorType!=null ? qmbtwhy.mbtiCategory.eq(mbtiCategory) : null).fetch();
	}
	
	// 게시글 개수 조회 (MBTI 타입, 검색 값, 정렬 옵션)
	public Long findMbtwhyCountByMbtiAndSearchAndSort(String mbti, String search, String sort) throws Exception {

		return jpaQueryFactory.select(mbtwhy.count())
						.from(mbtwhy)
						.where(search!=null? mbtwhy.content.containsIgnoreCase(search) : null,
								sort!=null? mbtwhy.content.containsIgnoreCase(sort) : null,
										mbtwhy.isBlocked.eq("N"),
										mbtwhy.mbtiCategory.eq(mbti)).fetchOne();
	}
	
	// 게시글 조회 (게시글 번호)
	public Mbtwhy findMbtwhyByNo(Integer no) throws Exception {
		return jpaQueryFactory.selectFrom(mbtwhy)
						.where(mbtwhy.no.eq(no)).fetchOne();
	}
	
}
