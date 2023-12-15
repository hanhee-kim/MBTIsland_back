package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Mbtwhy;

public interface MbtwhyRepository extends JpaRepository<Mbtwhy, Integer> {
	Mbtwhy findByNo(Integer no) throws Exception; // 게시글 조회 (인덱스)
}
