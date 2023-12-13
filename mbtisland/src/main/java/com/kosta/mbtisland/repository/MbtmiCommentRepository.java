package com.kosta.mbtisland.repository;

import static com.kosta.mbtisland.entity.QMbtmiComment.mbtmiComment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbtmiComment;

public interface MbtmiCommentRepository extends JpaRepository<MbtmiComment, Integer> {
	
	// 주의: 항상 where절에 mbtmiComment.isBlocked.eq("N")를 포함해야하기 때문에 MbtmiCommentRepository의 countBy명명규칙을 통해 자동생성되는 쿼리를 이용하여 댓글수를 조회하지 않도록 한다
	
//	Long countByMbtmiNo(Integer mbtmiNo);
}
