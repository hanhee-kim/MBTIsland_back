package com.kosta.mbtisland.repository;

import java.util.List;

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

	//
	public List<Bookmark> findByUsernameAndPaging(String username,PageRequest pageRequest){
		QBookmark qBookmark = QBookmark.bookmark;
		return jpaQueryFactory.selectFrom(qBookmark)
					.where(qBookmark.username.eq(username))
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
	}
	
	public Long findCntBookmarkListByuser(String username) {
		QBookmark qBookmark = QBookmark.bookmark;
		return jpaQueryFactory.select(qBookmark.count())
				.from(qBookmark)
				.where(qBookmark.username.eq(username))
				.fetchOne();
	}
	
	
}
