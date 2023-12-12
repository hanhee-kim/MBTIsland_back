package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Mbtmi;

public interface MbtmiRepository extends JpaRepository<Mbtmi, Integer> {
	
	Long countByCategory(String category);
	
}
