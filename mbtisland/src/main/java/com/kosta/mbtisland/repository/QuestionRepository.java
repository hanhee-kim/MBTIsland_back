package com.kosta.mbtisland.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	Long countByIsAnswered(String criteria); // "N" 또는 "Y"
	List<Question> findByWriterId(String writerId);
	Long countByWriterId(String username);
	
}
