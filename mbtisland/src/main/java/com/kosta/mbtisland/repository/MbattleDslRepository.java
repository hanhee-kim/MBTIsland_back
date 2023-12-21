package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbattleComment.mbattleComment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.MbattleComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbattleDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 댓글 목록 조회 (게시글 번호)
	public List<MbattleComment> findMbattleCommentListByMbattleNoAndPage(Integer no, PageRequest pageRequest) {
		return jpaQueryFactory.selectFrom(mbattleComment)
				.where(mbattleComment.mbattleNo.eq(no))
				.orderBy(mbattleComment.commentNo.asc()) // 정렬
				.offset(pageRequest.getOffset()) // 인덱스
				.limit(pageRequest.getPageSize()) // 개수 제한
				.fetch();
	}
}
