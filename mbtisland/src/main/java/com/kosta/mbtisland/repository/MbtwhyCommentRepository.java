package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbtwhyComment;

public interface MbtwhyCommentRepository extends JpaRepository<MbtwhyComment, Integer> {
	Integer countByMbtwhyNo(Integer no);
}
