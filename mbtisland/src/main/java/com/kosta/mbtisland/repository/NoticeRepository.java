package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosta.mbtisland.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	Long countByIsHided(String criteria); // "N" 또는 "Y"
	
}
