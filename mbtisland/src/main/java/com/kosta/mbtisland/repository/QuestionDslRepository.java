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
	
	// 1. 문의글 목록 일반
	// 문의글 목록 (검색, 필터, 페이징)
	public List<Question> findQuestionListBySearchAndFilterAndPaging(String searchTerm, String isAnswered, PageRequest pageRequest) {
		return jpaQueryfactory.selectFrom(question)
						.where(
								isAnswered!=null? question.isAnswered.eq(isAnswered) : null,
								searchTerm!=null? question.title.containsIgnoreCase(searchTerm)
										.or(question.content.containsIgnoreCase(searchTerm)) : null
						)
						.orderBy(question.no.desc())
						.offset(pageRequest.getOffset()) // 시작행의 위치
						.limit(pageRequest.getPageSize()) // 페이지당 항목 수
						.fetch();
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
	
	
	// 2. 특정 유저의 문의글 모아보기
	// 특정 유저의 문의글 목록
	public List<Question> findQuestionListByUserAndFilterAndPaging(String userId, String isAnswered, PageRequest pageRequest) {
		return jpaQueryfactory.selectFrom(question)
						.where(
								question.writerId.eq(userId),
								isAnswered!=null? question.isAnswered.eq(isAnswered) : null
						)
						.orderBy(question.no.desc())
						.offset(pageRequest.getOffset()) // 시작행의 위치
						.limit(pageRequest.getPageSize()) // 페이지당 항목 수
						.fetch();
	}
	// 아이디적용된 totalCnt 조회
	public Long countByUser(String userId) {
		return jpaQueryfactory.select(question.count()).from(question)
								.where(question.writerId.eq(userId))
								.fetchOne();
	}
	// 아이디적용된 answeredCnt 조회
	public Long countByUserPlusAnswered(String userId) {
		return jpaQueryfactory.select(question.count()).from(question)
								.where(
										question.isAnswered.eq("Y")
											.and(question.writerId.eq(userId))
										)
								.fetchOne();
	}
	// 아이디적용된 answeredNotCnt 조회
	public Long countByUserPlusAnsweredNot(String userId) {
		return jpaQueryfactory.select(question.count()).from(question)
				.where(
						question.isAnswered.eq("N")
						.and(question.writerId.eq(userId))
						)
				.fetchOne();
	}

}
