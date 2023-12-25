package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtmi.mbtmi;
import static com.kosta.mbtisland.entity.QMbtmiComment.mbtmiComment;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtmi;
import com.kosta.mbtisland.entity.MbtmiComment;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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
	
	
	// 2. 최신글 목록 (카테고리, 타입, 검색, 페이징, 정렬, 작성자)
	public List<Mbtmi> findNewlyMbtmiListByCategoryAndTypeAndSearchAndPaging(String category, String type, String searchTerm, PageRequest pageRequest, String sort, String username) {
//		System.out.println("dsl의 파라미터 정렬값: " + sort);
	    JPAQuery<Mbtmi> query = jpaQueryfactory.selectFrom(mbtmi)
	            .where(
	                    category != null ? mbtmi.category.eq(category) : null,
	                    type != null ? isWriterMbtiContainsStr(type) : null,
	                    searchTerm != null ? mbtmi.title.containsIgnoreCase(searchTerm)
	                            .or(mbtmi.content.containsIgnoreCase(searchTerm)) : null,
	                    username != null? mbtmi.writerId.eq(username) : null,
	                    mbtmi.isBlocked.eq("N")
	            );

	    if(sort==null) {
	    	query.orderBy(mbtmi.no.desc());
	    } else {
	    	query.orderBy(
	    			sort.equals("최신순") ? mbtmi.no.desc() : 
	    			sort.equals("조회순") ? mbtmi.viewCnt.desc() : 
	    			sort.equals("추천순") ? mbtmi.recommendCnt.desc() : null
	    			);
	    }

	    return query.offset(pageRequest.getOffset())
	            .limit(pageRequest.getPageSize())
	            .fetch();
	}
	
	// 2-1. where절에서 사용할 검색조건의 일부를 BooleanExpression로 반환하는 메서드 선언
	private BooleanExpression isWriterMbtiContainsStr(String type) {						
		BooleanExpression strCondition = null;
	    
	    for (int j=0; j<type.length(); j++) { // 문자열type의 길이만큼 반복
	        BooleanExpression charCondition  = mbtmi.writerMbti.containsIgnoreCase(String.valueOf(type.charAt(j)));
	        strCondition = (strCondition == null)? charCondition : strCondition.and(charCondition); // 1회차(0인덱스)는 한번비교, 2회차부터는 &&로 비교
	        
//	        System.out.println("===> " + strCondition); 
	        /* 출력예시 type="IST"인 경우
	         ===> containsIc(mbtmi.writerMbti,I)
			 ===> containsIc(mbtmi.writerMbti,I) && containsIc(mbtmi.writerMbti,S)
			 ===> containsIc(mbtmi.writerMbti,I) && containsIc(mbtmi.writerMbti,S) && containsIc(mbtmi.writerMbti,T)
	        */
	    }
	    return strCondition;
	}
	
	// 3. 최신글수 조회 (PageInfo의 allPage값 계산시 필요)
	public Long countByCategoryPlusWriterMbtiPlusSearch(String category, String type, String searchTerm, String username) {

	    return jpaQueryfactory.select(mbtmi.count()).from(mbtmi)
	    		.where(
	    				category!=null? mbtmi.category.eq(category) : null,
	    				type!=null? isWriterMbtiContainsStr(type) : null,
	    				searchTerm!=null? mbtmi.title.containsIgnoreCase(searchTerm)
				    					.or(mbtmi.content.containsIgnoreCase(searchTerm)) : null,
				    	username != null? mbtmi.writerId.eq(username) : null,
				    	mbtmi.isBlocked.eq("N")
	    				)
	    		.fetchOne();
	}

	
	// 4. 특정 게시글의 댓글 목록
	public List<MbtmiComment> findMbtmiCommentListByMbtmiNoAndPaging(Integer mbtmiNo, PageRequest pageRequest) {
		return jpaQueryfactory.selectFrom(mbtmiComment)
								.where(mbtmiComment.mbtmiNo.eq(mbtmiNo))
//								.orderBy(mbtmiComment.writeDate.asc())
								.orderBy(mbtmiComment.parentcommentNo.coalesce(mbtmiComment.commentNo).asc(), mbtmiComment.commentNo.asc())
								.offset(pageRequest.getOffset())
								.limit(pageRequest.getPageSize())
								.fetch();
	}
	
	
	// 5. 특정 게시글의 댓글수 조회 (PageInfo의 allPage값 계산시 필요)
	public Long countCommentByMbtmiNo(Integer mbtmiNo) {
	    return jpaQueryfactory
	            .select(mbtmiComment.count())
	            .from(mbtmiComment)
	            .where(
	                    mbtmiComment.mbtmiNo.eq(mbtmiNo)
//	                        .and(mbtmiComment.isBlocked.eq("N"))
//	                        .and(mbtmiComment.isRemoved.eq("N"))
	            )
	            .fetchOne();
	}
	
	// 6. 댓글의 대댓글 수 조회
	public Long countCommentByParentcommentNo(Integer mbtmiCommentNo) {
		return jpaQueryfactory
				.select(mbtmiComment.count())
				.from(mbtmiComment)
				.where(
						mbtmiComment.parentcommentNo.eq(mbtmiCommentNo)
				)
				.fetchOne();
	}

	
	// 7. 특정 게시글에 속한 댓글 삭제(게시글 삭제시 관련데이터를 함께 삭제하기 위해 호출)
	@Transactional
	public void deleteCommentsByMbtmiNo(Integer mbtmiNo) {
		jpaQueryfactory.delete(mbtmiComment)
						.where(mbtmiComment.mbtmiNo.eq(mbtmiNo))
						.execute();
	}
	
	// 8. 특정 게시글의 모든 댓글의 pk를 리스트로 반환(게시글 삭제시 관련데이터-대댓글알림-를 함께 삭제하기 위해 호출)
	public List<Integer> findCommentNosByPostNo(Integer postNo) {
		return jpaQueryfactory.select(mbtmiComment.commentNo)
								.from(mbtmiComment)
								.where(mbtmiComment.mbtmiNo.eq(postNo))
								.fetch();
	}
	
	
	
}
