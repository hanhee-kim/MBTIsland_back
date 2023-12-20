package com.kosta.mbtisland.service;

import com.kosta.mbtisland.dto.AnswerDto;
import com.kosta.mbtisland.entity.Answer;

public interface AnswerService {
	
	// 특정 질문글에 대한 답글 조회
	Answer selectAnswerByQuestionNo(Integer questionNo) throws Exception;
	
	// 답글 등록
	Answer addAnswer(AnswerDto answerDto) throws Exception;

}
