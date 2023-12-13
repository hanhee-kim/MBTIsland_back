package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Mbtmi;

public interface MbtmiRepository extends JpaRepository<Mbtmi, Integer> {
	
	Long countByCategory(String category);
	Long countByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String searchTerm1, String searchTerm2); // 매개변수가 두개가 아니라면 선언규칙 위배로 디펜던시 오류 발생
	
}
