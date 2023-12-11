package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.MbtmiComment;

public interface MbtmiCommentRepository extends JpaRepository<MbtmiComment, Integer> {
	
	Long countByMbtmiNo(Integer mbtmiNo);
}
