package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbattleComment;

public interface MbattleCommentRepository extends JpaRepository<MbattleComment, Integer>{
	Integer countByMbattleNo(Integer no); // 댓글 개수 조회 (인덱스)
}
