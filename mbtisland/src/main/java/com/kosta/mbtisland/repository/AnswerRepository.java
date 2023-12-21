package com.kosta.mbtisland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{
	Optional<Answer> findByQuestionNo(Integer QuestionNo) throws Exception; // 특정 질문글의 답글 조회
	
	
	
	
}
