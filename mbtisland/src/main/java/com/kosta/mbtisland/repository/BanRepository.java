package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Ban;

public interface BanRepository extends JpaRepository<Ban, Integer> {
	
	Ban findByUsername(String username) throws Exception;

}
