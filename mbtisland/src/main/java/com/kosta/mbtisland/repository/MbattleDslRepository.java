package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbattle.mbattle;
import static com.kosta.mbtisland.entity.QMbattleComment.mbattleComment;
import static com.kosta.mbtisland.entity.QMbattleResult.mbattleResult;
import static com.kosta.mbtisland.entity.QMbattleVoter.mbattleVoter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbattle;
import com.kosta.mbtisland.entity.MbattleComment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbattleDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 게시글 목록 조회 (특정 페이지, 검색 값, 정렬 옵션)
	public List<Mbattle> findMbattleListByPageAndSearchAndSort
		(PageRequest pageRequest, String search, String sort) throws Exception {
		OrderSpecifier<?> orderSpecifier;

		// 정렬 조건
		if(sort==null) {
			orderSpecifier = mbattle.no.desc();
		} else if(sort.equals("최신순")) { // 최신순
			orderSpecifier = mbattle.no.desc();
		} else if(sort.equals("조회순")) { // 조회순
			orderSpecifier = mbattle.viewCnt.desc();
		} else if(sort.equals("투표순")) { // 투표순
			orderSpecifier = mbattle.voteCnt.desc();
		} 
		else { // 기본 최신순
			orderSpecifier = mbattle.no.desc();
		}

		return jpaQueryFactory.selectFrom(mbattle)
				.where(search!=null? mbattle.title.containsIgnoreCase(search) : null,
						mbattle.isBlocked.eq("N"))
						.orderBy(orderSpecifier) // 정렬
						.offset(pageRequest.getOffset()) // 인덱스
						.limit(pageRequest.getPageSize()) // 개수 제한
						.fetch();
	}
	
	// 일간 인기 게시글 조회
	public List<Mbattle> findDailyHotMbattle() throws Exception {
		// 현재 날짜
		LocalDate currentDate = LocalDate.now();
		Timestamp startDate = Timestamp.valueOf(currentDate.atStartOfDay());
		
		return jpaQueryFactory.select(mbattle)
				.from(mbattle)
				.where(mbattle.writeDate.after(startDate),
						mbattle.isBlocked.eq("N"))
				.orderBy(mbattle.voteCnt.desc())
				.limit(2)
				.fetch();
	}
	
	// 게시글 개수 조회 (검색 값)
	public Long findMbattleCountBySearch(String search) throws Exception {
		return jpaQueryFactory.select(mbattle.count())
						.from(mbattle)
						.where(search!=null? mbattle.title.containsIgnoreCase(search) : null,
								mbattle.isBlocked.eq("N"))
						.fetchOne();
	}
	
	// 랜덤 게시글 조회
	public Integer findRandomMbattleNo() throws Exception {
		return jpaQueryFactory.select(mbattle.no)
				.from(mbattle)
				.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
				.fetchFirst();
	}
	
	// 댓글 목록 조회 (게시글 번호)
	public List<MbattleComment> findMbattleCommentListByMbattleNoAndPage(Integer no, PageRequest pageRequest) throws Exception {
		return jpaQueryFactory.selectFrom(mbattleComment)
				.where(mbattleComment.mbattleNo.eq(no))
				.orderBy(mbattleComment.commentNo.asc()) // 정렬
				.offset(pageRequest.getOffset()) // 인덱스
				.limit(pageRequest.getPageSize()) // 개수 제한
				.fetch();
	}
	
	// 특정 게시글에 속한 댓글 삭제(게시글 삭제시 관련데이터를 함께 삭제하기 위해 호출)
	@Transactional
	public void deleteCommentsByMbattleNo(Integer mbattleNo) {
		jpaQueryFactory.delete(mbattleComment)
						.where(mbattleComment.mbattleNo.eq(mbattleNo))
						.execute();
	}
	
	// 특정 게시글에 속한 MbattleResult 삭제
	@Transactional
	public void deleteResultsByMbattleNo(Integer mbattleNo) {
		jpaQueryFactory.delete(mbattleResult)
						.where(mbattleResult.mbattleNo.eq(mbattleNo))
						.execute();
	}
	
	// 특정 게시글에 속한 MbattleVoter 삭제
	@Transactional
	public void deleteVotersByMbattleNo(Integer mbattleNo) {
		jpaQueryFactory.delete(mbattleVoter)
						.where(mbattleVoter.mbattleNo.eq(mbattleNo))
						.execute();
	}
	
	public List<Mbattle> findMbattleListByUserAndPage (String username,PageRequest pageRequest){
		List<Mbattle> mbattleList =  jpaQueryFactory
				.select(mbattle)
				.from(mbattle)
				.where(mbattle.writerId.eq(username))
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
		return mbattleList;
	}
	
	public Long findMbattleCntByUser(String username) {
		Long cnt = jpaQueryFactory
				.select(mbattle.count())
				.from(mbattle)
				.where(mbattle.writerId.eq(username))
				.fetchOne();
		return cnt;
	}
	
	
}
