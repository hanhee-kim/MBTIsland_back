package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QUserEntity.userEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.entity.Mbtwhy;
import com.kosta.mbtisland.entity.QMbtwhy;
import com.kosta.mbtisland.entity.QUserEntity;
import com.kosta.mbtisland.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	//mbtwhyRepository에서 가져오면대는데..?
	public List<Mbtwhy> findMbtwhyListByUsername(String username) throws Exception{
		QUserEntity user = QUserEntity.userEntity;
		QMbtwhy mbtwhy = QMbtwhy.mbtwhy;
		
		List<Mbtwhy> mbtwhyList = jpaQueryFactory.select(mbtwhy)
				.from(mbtwhy)
				.join(user)
				.on(user.username.eq(mbtwhy.writerId))
				.where(mbtwhy.writerId.eq(username))
				.fetch();
		return mbtwhyList;
	}
	
	//특정 페이지의 내 mbtwhy게시글 목록 가져오기
		public List<Mbtwhy> findMyMbtwhyListByPaging(PageRequest pageRequest,String username) throws Exception{
			//옵셋과 리밋이 있다?
			QMbtwhy mbtwhy = QMbtwhy.mbtwhy;
			return jpaQueryFactory.selectFrom(mbtwhy)
					.where(mbtwhy.writerId.eq(username))
					.orderBy(mbtwhy.no.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		}
	
	// 경고된 모든 유저 목록 조회
	public List<UserEntity> findBannedUserListByPageAndFilterAndUsername(PageRequest pageRequest, String filter, String username) throws Exception {
		return jpaQueryFactory.selectFrom(userEntity)
				.where(filter.equals("all")? null : userEntity.isBanned.eq(filter),
						username.equals("")? null : userEntity.username.containsIgnoreCase(username),
						userEntity.userWarnCnt.gt(0))
						.orderBy(userEntity.userIdx.desc()) // 정렬
						.offset(pageRequest.getOffset()) // 인덱스
						.limit(pageRequest.getPageSize()) // 개수 제한
						.fetch();
	}
	
	// 경고된 모든 유저 목록 개수
	public Long findBanCountByFilterAndUsername(String filter, String username) throws Exception {
		return jpaQueryFactory.select(userEntity.count())
				.from(userEntity)
				.where(filter.equals("all")? null : userEntity.isBanned.eq(filter),
						username.equals("")? null : userEntity.username.containsIgnoreCase(username),
						userEntity.userWarnCnt.gt(0))
				.orderBy(userEntity.userIdx.desc())
				.fetchOne();
	}
	
	// 경고된 유저 조회 (단일)
	public UserEntity findBannedUserByUsername(String username) throws Exception {
		return jpaQueryFactory.selectFrom(userEntity)
				.where(userEntity.username.eq(username),
						userEntity.userWarnCnt.gt(0))
						.fetchOne();
	}
}
