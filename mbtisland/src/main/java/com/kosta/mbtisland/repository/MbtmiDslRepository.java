package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtmi.mbtmi;
import static com.kosta.mbtisland.entity.QMbtmiComment.mbtmiComment;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MbtmiDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
		
	// 1. 주간인기글 목록
	
	// 1-1. 각 카테고리별 최다 추천수 조회
	public List<Tuple> findMaxRecommendCntByCategory() {
		// 현재 날짜로부터 6일을 뺀 startDate를 계산
		LocalDate today = LocalDate.now();
		LocalDate todayMinus6Days = today.minusDays(6);
		Timestamp startDate = Timestamp.valueOf(todayMinus6Days.atStartOfDay()); // Timestamp타입으로 변환
		
		// 다중열 다중행이므로 List<Tuple>로 반환
		return jpaQueryfactory
                .select(mbtmi.category, mbtmi.recommendCnt.max())
                .from(mbtmi)
                .where(mbtmi.writeDate.after(startDate),
                		mbtmi.isBlocked.eq("N"))
                .groupBy(mbtmi.category)
                .orderBy(mbtmi.recommendCnt.max().desc())
                .fetch();
	}

	// 1-2. 각 카테고리별 최다 추천수를 가진 게시글 조회
	public List<Mbtmi> findWeeklyHotMbtmiListByCategoryAndRecommendCnt() {
	    // 현재 날짜로부터 6일을 뺀 startDate를 계산
	    LocalDate today = LocalDate.now();
	    LocalDate todayMinus6Days = today.minusDays(6);
	    Timestamp startDate = Timestamp.valueOf(todayMinus6Days.atStartOfDay()); // Timestamp타입으로 변환

	    List<Tuple> maxRecommendCntByCategory = findMaxRecommendCntByCategory(); // 각 카테고리별 최다 추천수 조회하여 담은 리스트

	    List<Mbtmi> result = new ArrayList<>(); // 각 카테고리별 최다 추천수를 가진 게시글을 조회하여 담을 리스트

	    for (Tuple tuple : maxRecommendCntByCategory) {
	        String category = tuple.get(mbtmi.category);
	        Integer maxRecommendCnt = tuple.get(mbtmi.recommendCnt.max());

	        Mbtmi maxRecommendMbtmi = jpaQueryfactory
						                .selectFrom(mbtmi)
						                .where(mbtmi.category.eq(category),
						                        mbtmi.recommendCnt.eq(maxRecommendCnt),
						                        mbtmi.isBlocked.eq("N")
						                )
						                .fetchFirst();

	        if (maxRecommendMbtmi != null) {
	            result.add(maxRecommendMbtmi);
	        }
	    }
	    return result;
	}
	
	
	// 2. 최신글 목록 (카테고리, 타입, 검색, 페이징)
	public List<Mbtmi> findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(String category, String type, String searchTerm, PageRequest pageRequest) {
		return jpaQueryfactory.selectFrom(mbtmi)
							.where(
									category!=null? mbtmi.category.eq(category) : null,
									type!=null? isContainMbtiStr(type) : null,
									searchTerm!=null? mbtmi.title.containsIgnoreCase(searchTerm)
											.or(mbtmi.content.containsIgnoreCase(searchTerm)) : null,
									mbtmi.isBlocked.eq("N")
							)
							.orderBy(mbtmi.no.desc())
							.offset(pageRequest.getOffset())
							.limit(pageRequest.getPageSize())
							.fetch();
	}
	
	// where메서드의 매개변수로 BooleanExpression를 사용하여 검색조건 분리
	private BooleanExpression isContainMbtiStr(String type) {		

/*
		// 전달인자로 받은 type(예시 "I-P")을 구분자로 잘라 문자열배열에 담는다
		StringTokenizer st = new StringTokenizer(type, "-");
		String[] sArr = new String[st.countTokens()];
		int i=0;
		while(st.hasMoreTokens()) {
			sArr[i++] = st.nextToken();
		}
*/
		
		// ex. String type = "ISFJ";
		String[] sArr = new String[type.length()];

		for (int i=0; i<type.length(); i++) {
		    sArr[i] = String.valueOf(type.charAt(i));
		}
		
		
		// 배열에 담긴 문자열이 모두 포함되는 경우에만 true를 반환한다
		BooleanExpression isContain = null;		
		for (int j=0; j+1<sArr.length; j++) {
			isContain = mbtmi.writerMbti.containsIgnoreCase(sArr[j]).and(mbtmi.writerMbti.containsIgnoreCase(sArr[j+1]));
		}
		return isContain;
	}
	
	
	
	
	
	
	
	
	
	
	
	// 카테고리 && 타입 적용된 게시글수
	public Long countByCategoryPlusWriterMbti(String category, String type) {
		return jpaQueryfactory.select(mbtmi.count()).from(mbtmi)
				.where(
						mbtmi.category.eq(category)
						.and(mbtmi.writerMbti.containsIgnoreCase(type))
						)
				.fetchOne();
	}
	
	// 카테고리 && 타입 && 검색어 적용된 게시글수
	public Long countByCategoryPlusWriterMbtiPlusSearch(String category, String type, String searchTerm) {
		return jpaQueryfactory.select(mbtmi.count()).from(mbtmi)
				.where(
						mbtmi.category.eq(category)
						.and(mbtmi.writerMbti.containsIgnoreCase(type))
						.and(mbtmi.title.containsIgnoreCase(searchTerm)
								.or(mbtmi.content.containsIgnoreCase(searchTerm)))
						)
				.fetchOne();
	}
	
	// 특정 게시글의 댓글 목록
	public List<MbtmiComment> findMbtmiCommentListByMbtmiNoAndPaging(Integer mbtmiNo, PageRequest pageRequest) {
		return jpaQueryfactory.selectFrom(mbtmiComment)
								.where(mbtmiComment.mbtmiNo.eq(mbtmiNo)
										.and(mbtmiComment.isBlocked.eq("N")))
								.orderBy(mbtmiComment.writeDate.asc())
								.offset(pageRequest.getOffset())
								.limit(pageRequest.getPageSize())
								.fetch();
	}

	
}
