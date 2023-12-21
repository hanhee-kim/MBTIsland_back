package com.kosta.mbtisland.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.AnswerDto;
import com.kosta.mbtisland.entity.Answer;
import com.kosta.mbtisland.repository.AnswerRepository;

@Service
public class AnswerServiceImpl implements AnswerService {
	
	@Autowired
	private AnswerRepository answerRepository;

	// 특정 질문글에 대한 답글 조회
	@Override
	public Answer selectAnswerByQuestionNo(Integer questionNo) throws Exception {
		Optional<Answer> oanswer = answerRepository.findByQuestionNo(questionNo);
		if(oanswer.isPresent()) return oanswer.get();
		else return null;
	}

	// 답글 등록
	@Override
	public Answer addAnswer(AnswerDto answerDto) throws Exception {
		LocalDate currentDate = LocalDate.now();
		Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
		Answer answer = Answer.builder()
							.title(answerDto.getTitle())
							.content(answerDto.getContent())
							.writeDate(writeDate)
							.writerId(answerDto.getWriterId())
							.questionNo(answerDto.getQuestionNo())
							.fileIdxs(answerDto.getFileIdxs())
							.build();
		answerRepository.save(answer);
		Optional<Answer> oanswer = answerRepository.findById(answer.getAnswerNo());
		if(oanswer.isPresent()) return oanswer.get();
		else return null;
	}

}
