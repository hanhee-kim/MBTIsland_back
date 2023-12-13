package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Question;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.kosta.mbtisland.entity.QQuestion.question;

@Repository
public class QuestionDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
	
	
	/* 관리자페이지 */
	
	// 문의글 목록 (검색, 필터, 페이징)
	public List<Question> findQuestionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageRequest pageRequest, String username) {
		return jpaQueryfactory.selectFrom(question)
						.where(
								isAnswered!=null? question.isAnswered.eq(isAnswered) : null,
								searchTerm!=null? question.title.containsIgnoreCase(searchTerm)
										.or(question.content.containsIgnoreCase(searchTerm)) : null,
								username!=null? question.writerId.eq(username) : null
						)
						.orderBy(question.no.desc())
						.offset(pageRequest.getOffset()) // 시작행의 위치
						.limit(pageRequest.getPageSize()) // 페이지당 항목 수
						.fetch();
	}
	
	// 게시글수 조회 (PageInfo의 allPage값 계산시 필요)
	public Long countByAnsweredPlusSearchPlusWriterId(String isAnswered, String searchTerm, String username) {
		return jpaQueryfactory.select(question.count()).from(question)
				.where(
						isAnswered!=null? question.isAnswered.eq(isAnswered) : null,
						searchTerm!=null? question.title.containsIgnoreCase(searchTerm)
								.or(question.content.containsIgnoreCase(searchTerm)) : null,
						username!=null? question.writerId.eq(username) : null
						)
				.fetchOne();
	}


	// 검색적용된 totalCnt 조회
	public Long countBySearchTerm(String searchTerm) {
		return jpaQueryfactory.select(question.count()).from(question)
								.where(question.title.containsIgnoreCase(searchTerm)
										.or(question.content.containsIgnoreCase(searchTerm)))
								.fetchOne();
	}
	// 검색적용된 answeredCnt 조회
	public Long countBySearchTermPlusAnswered(String searchTerm) {
		return jpaQueryfactory.select(question.count()).from(question)
								.where(
										question.isAnswered.eq("Y")
											.and(question.title.containsIgnoreCase(searchTerm)
													.or(question.content.containsIgnoreCase(searchTerm)))
										)
								.fetchOne();
	}
	// 검색적용된 answeredNotCnt 조회
	public Long countBySearchTermPlusAnsweredNot(String searchTerm) {
		return jpaQueryfactory.select(question.count()).from(question)
				.where(
						question.isAnswered.eq("N")
						.and(question.title.containsIgnoreCase(searchTerm)
								.or(question.content.containsIgnoreCase(searchTerm)))
						)
				.fetchOne();
	}
	
	
	
	
	
	
	
	
	
	
	
	/* 마이페이지 */
	

}
