package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtmi.mbtmi;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtmi;
import com.querydsl.core.Tuple;
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
                .where(mbtmi.writeDate.after(startDate))
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
	                .where(mbtmi.category.eq(category)
	                        .and(mbtmi.recommendCnt.eq(maxRecommendCnt)))
	                .fetchFirst();

	        if (maxRecommendMbtmi != null) {
	            result.add(maxRecommendMbtmi);
	        }
	    }
	    return result;
	}
	
	
}
