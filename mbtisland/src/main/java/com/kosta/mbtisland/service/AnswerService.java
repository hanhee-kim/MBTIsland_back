package com.kosta.mbtisland.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Answer;

public interface AnswerService extends JpaRepository<Answer, Integer>{
	Optional<Answer> findByQuestionNo(Integer QuestionNo) throws Exception;
	
	
	
	
}
