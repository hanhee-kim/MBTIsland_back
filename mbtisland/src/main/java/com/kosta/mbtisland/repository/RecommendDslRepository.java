package com.kosta.mbtisland.repository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.kosta.mbtisland.entity.QRecommend.recommend;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class RecommendDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// 특정 게시글과 관련된 추천 삭제(게시글 삭제시 호출됨)
	@Transactional
	public void deleteRecommendByPostNoAndBoardType(Integer postNo, String boardType) {
		jpaQueryFactory.delete(recommend)
						.where(
								recommend.postNo.eq(postNo),
								recommend.boardType.lower().eq(boardType.toLowerCase())
								)
						.execute();
	}

}
