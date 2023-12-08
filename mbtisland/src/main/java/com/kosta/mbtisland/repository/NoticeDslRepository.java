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
	
	
	// 검색적용된 totalCnt 조회
	public Long countBySearchTerm(String searchTerm) {
		return jpaQueryfactory.select(notice.count()).from(notice)
								.where(notice.title.containsIgnoreCase(searchTerm)
										.or(notice.content.containsIgnoreCase(searchTerm)))
								.fetchOne();
	}
	// 검색적용된 displayCnt 조회
	public Long countBySearchTermPlusDisplay(String searchTerm) {
		return jpaQueryfactory.select(notice.count()).from(notice)
								.where(
										notice.isHided.eq("N")
											.and(notice.title.containsIgnoreCase(searchTerm)
													.or(notice.content.containsIgnoreCase(searchTerm)))
										)
								.fetchOne();
	}
	// 검색적용된 hiddenCnt 조회
	public Long countBySearchTermPlusHidden(String searchTerm) {
		return jpaQueryfactory.select(notice.count()).from(notice)
				.where(
						notice.isHided.eq("Y")
						.and(notice.title.containsIgnoreCase(searchTerm)
								.or(notice.content.containsIgnoreCase(searchTerm)))
						)
				.fetchOne();
	}

	

}
