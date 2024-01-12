package com.kosta.mbtisland.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Bookmark;
import com.kosta.mbtisland.entity.QBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class BookmarkDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	// 특정 사용자의 북마크 목록 조회 (페이징)
	public List<Bookmark> findByUsernameAndPaging(String username,PageRequest pageRequest){
		QBookmark qBookmark = QBookmark.bookmark;
		return jpaQueryFactory.selectFrom(qBookmark)
					.where(qBookmark.username.eq(username))
					.orderBy(qBookmark.no.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
	}
	
	// 특정 사용자의 북마크 목록 개수 조회
	public Long findCntBookmarkListByuser(String username) {
		QBookmark qBookmark = QBookmark.bookmark;
		return jpaQueryFactory.select(qBookmark.count())
				.from(qBookmark)
				.where(qBookmark.username.eq(username))
				.fetchOne();
	}
	
	// 특정 게시글과 관련된 북마크 삭제(게시글 삭제시 호출됨)
	@Transactional
	public void deleteBookmarkByPostNoAndBoardType(Integer postNo, String boardType) {
		QBookmark qBookmark = QBookmark.bookmark;
		jpaQueryFactory.delete(qBookmark)
						.where(
								qBookmark.postNo.eq(postNo),
								qBookmark.boardType.lower().eq(boardType.toLowerCase())
								)
						.execute();
	}
	
}
