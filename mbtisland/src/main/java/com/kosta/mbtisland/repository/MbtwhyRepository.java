package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Mbtwhy;

public interface MbtwhyRepository extends JpaRepository<Mbtwhy, Integer> {
	Mbtwhy findByNo(Integer no);
	List<Mbtwhy> findByWriterId(String username)throws Exception;
	Integer countByWriterId(String username) throws Exception;
}
