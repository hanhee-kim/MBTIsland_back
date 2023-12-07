package com.kosta.mbtisland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	Long countByIsHided(String criteria); // "N" 또는 "Y"
}
