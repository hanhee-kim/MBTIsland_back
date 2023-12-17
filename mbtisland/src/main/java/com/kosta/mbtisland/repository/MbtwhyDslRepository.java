package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtwhy.mbtwhy;
import static com.kosta.mbtisland.entity.QMbtwhyComment.mbtwhyComment;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.MbtwhyComment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbtwhyDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 게시글 목록 조회 (MBTI 타입, 특정 페이지, 검색 값, 정렬 옵션)
	public List<Mbtwhy> findMbtwhyListByMbtiAndPageAndSearchAndSort
		(String mbti, PageRequest pageRequest, String search, String sort) throws Exception {
		OrderSpecifier<?> orderSpecifier;
		System.out.println(sort);
		
		// 정렬 조건
		if(sort.equals("최신순")) { // 최신순
			orderSpecifier = mbtwhy.no.desc();
		} else if(sort.equals("조회순")) { // 조회순
			orderSpecifier = mbtwhy.viewCnt.desc();
		} else if(sort.equals("추천순")) { // 추천순
			orderSpecifier = mbtwhy.recommendCnt.desc();
		} else { // 기본 최신순
			orderSpecifier = mbtwhy.no.desc();
		}
		
		return jpaQueryFactory.selectFrom(mbtwhy)
				.where(search!=null? mbtwhy.content.containsIgnoreCase(search) : null,
						mbtwhy.isBlocked.eq("N"),
						mbtwhy.mbtiCategory.eq(mbti))
						.orderBy(orderSpecifier) // 정렬
						.offset(pageRequest.getOffset()) // 인덱스
						.limit(pageRequest.getPageSize()) // 개수 제한
						.fetch();
	}
	
	// 게시글 개수 조회 (MBTI 타입, 검색 값)
	public Long findMbtwhyCountByMbtiAndSearch(String mbti, String search) throws Exception {
		
		return jpaQueryFactory.select(mbtwhy.count())
						.from(mbtwhy)
						.where(search!=null? mbtwhy.content.containsIgnoreCase(search) : null,
										mbtwhy.isBlocked.eq("N"),
										mbtwhy.mbtiCategory.eq(mbti)).fetchOne();
	}
	
	// 일간 인기 게시글 조회 (MBTI)
	public Mbtwhy findDailyHotMbtwhy(String mbti) throws Exception {
		// 현재 날짜
		LocalDate currentDate = LocalDate.now();
		Timestamp startDate = Timestamp.valueOf(currentDate.atStartOfDay());
		
		return jpaQueryFactory.select(mbtwhy)
				.from(mbtwhy)
				.where(mbtwhy.writeDate.after(startDate),
						mbtwhy.mbtiCategory.eq(mbti),
						mbtwhy.isBlocked.eq("N"))
				.orderBy(mbtwhy.recommendCnt.desc())
				.limit(1).fetchOne();
	}
	
	// 댓글 목록 조회 (게시글 번호)
	public List<MbtwhyComment> findMbtwhyCommentListByMbtwhyNoAndPage(Integer no, PageRequest pageRequest) {
		return jpaQueryFactory.selectFrom(mbtwhyComment)
				.where(mbtwhyComment.isBlocked.eq("N"), mbtwhyComment.isRemoved.eq("N"), mbtwhyComment.mbtwhyNo.eq(no))
				.orderBy(mbtwhyComment.commentNo.asc()) // 정렬
				.offset(pageRequest.getOffset()) // 인덱스
				.limit(pageRequest.getPageSize()) // 개수 제한
				.fetch();
	}

	// 댓글 개수 조회 (게시글 번호)
	public Long findMbtwhyCommentCountByMbtwhyNo(Integer no) {
		return jpaQueryFactory.select(mbtwhyComment.count()).from(mbtwhyComment)
				.where(mbtwhyComment.isBlocked.eq("N"), mbtwhyComment.isRemoved.eq("N"), mbtwhyComment.mbtwhyNo.eq(no))
				.fetchOne();
	}
	
	//
	public List<Mbtwhy> findMbtwhyListUserAndPaging(String username, PageRequest pageRequest){
		return jpaQueryFactory.selectFrom(mbtwhy)
//			.where(mbtwhy.writerId.eq(username).and(mbtwhy.isRemoved.eq("N"))
			.orderBy(mbtwhy.no.desc())
			.offset(pageRequest.getOffset())
			.limit(pageRequest.getPageSize())
			.fetch();
	}
	
//	public Long findMbtwhyCommentCountByMbtwhyNo(Integer no) {
//		return jpaQueryFactory.select(mbtwhyComment.count()).from(mbtwhyComment)
//				.where(mbtwhyComment.isBlocked.eq("N"), mbtwhyComment.isRemoved.eq("N"), mbtwhyComment.mbtwhyNo.eq(no))
//				.fetchOne();
//	}

}
