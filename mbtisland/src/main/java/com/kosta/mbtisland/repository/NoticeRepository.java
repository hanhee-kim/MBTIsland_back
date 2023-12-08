package com.kosta.mbtisland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	// 필터o, 검색어x
	Long countByIsHided(String criteria); // "N" 또는 "Y"
	
	// 필터x, 검색어o
	Long countByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String searchTerm1, String searchTerm2);
	
	// 필터o, 검색어o
	Long countByIsHidedAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String isHided, String searchTerm1, String searchTerm2);
}
