package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QNotice.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Notice;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


@Repository
public class NoticeDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
	
/*
	// 공지사항 목록 특정 페이지
	public List<Notice> findNoticeListByPaging(PageRequest pageRequest) throws Exception { // cf. 호출부인 서비스에서 PageRequest를 세팅
		
		QNotice notice = QNotice.notice;
		return jpaQueryfactory.selectFrom(notice)
								.orderBy(notice.no.desc())
								.offset(pageRequest.getOffset()) // 시작행의 위치
								.limit(pageRequest.getPageSize()) // 페이지당 항목수
								.fetch();
	}

	// 공지사항 목록 특정 페이지(숨김항목 제외)
	public List<Notice> findNotHiddenNoticeListByPaging(PageRequest pageRequest) throws Exception { // cf. 호출부인 서비스에서 PageRequest를 세팅
		
		QNotice notice = QNotice.notice;
		return jpaQueryfactory.selectFrom(notice)
				.where(notice.isHided.eq("N"))
				.orderBy(notice.no.desc())
				.offset(pageRequest.getOffset()) // 시작행의 위치
				.limit(pageRequest.getPageSize()) // 페이지당 항목수
				.fetch();
	}
*/	
	
	// 공지사항 목록 (검색, 필터, 페이징)
	public List<Notice> findNoticeListBySearchAndFilterAndPaging(String sValue, String isHided, PageRequest pageRequest) {
		JPAQuery<Notice> query = jpaQueryfactory.selectFrom(notice)
								.where(
									isHided!=null? notice.isHided.eq(isHided) : null,
											
									sValue!=null?
										notice.title.containsIgnoreCase(sValue)
										.or(notice.content.containsIgnoreCase(sValue)) : null
								)
								.orderBy(notice.no.desc())
								.offset(pageRequest.getOffset()) // 시작행의 위치
								.limit(pageRequest.getPageSize()); // 페이지당 항목수
		return query.fetch();
	}
	

	
	
	
	
	

}
