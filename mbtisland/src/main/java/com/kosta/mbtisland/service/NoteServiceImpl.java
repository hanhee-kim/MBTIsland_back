package com.kosta.mbtisland.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Alarm;
import com.kosta.mbtisland.entity.Note;
import com.kosta.mbtisland.repository.AlarmRepository;
import com.kosta.mbtisland.repository.NoteDslRepository;
import com.kosta.mbtisland.repository.NoteRepository;
import com.kosta.mbtisland.repository.UserRepository;
import com.querydsl.core.Tuple;

@Service
public class NoteServiceImpl implements NoteService{
	
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private NoteDslRepository noteDslRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AlarmRepository alarmRepository;
	

	//쪽지작성
	@Override
	public void noteWrite(NoteDto noteDto) throws Exception {
		Note note = Note.builder()
						.sentUsername(noteDto.getSentUsername())
						.noteContent(noteDto.getNoteContent())
						.receiveUsername(noteDto.getReceiveUsername())
						.build();
		noteRepository.save(note);
		//alarmTargetNo,alarmTargetFrom,alarmType이 같으면?
		//alarmNo찾아서 업데이트(isRead => N )
//		노트 작성하면 알림테이블에 추가 ( 위의 검색조건에 맞는 알림이 없다면)
		//쪽지는 같은 타겟넘버일 수없음. 쪽지가 발송될때마다 다른 쪽지이기 때문
		System.out.println(note.getNoteNo());
		Alarm alarm = Alarm.builder()
				.username(note.getReceiveUsername())
				.alarmType("쪽지")
				.alarmTargetNo(note.getNoteNo())
				.alarmTargetFrom("NOTE")
				.alarmReadDate(null)
				.build();
		
		alarmRepository.save(alarm);
//	readDate랑 updateDate 왜 NOT NULL	?		
	
		
	}
	
	//쪽지 불러오기(유저ID,noteType,readType,page)
	@Override
	public List<NoteDto> getNoteListByUsernameAndNoteTypeAndReadTypeAndPage(String username, String noteType,
		String readType, PageInfo pageInfo) throws Exception {
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		
		List<NoteDto> noteDtoList = new ArrayList<>();
		String sendRead;
		if(readType == null ||readType.equals("") || readType.equals("null")) {
			 sendRead = null;
		}else {
			 sendRead = readType;
		}
		Long allCnt = noteDslRepository.findNoteCntByUserAndNoteTypeAndReadType(username, noteType, sendRead);
		Long allCount = allCnt;
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		//필터로 적용한 페이지의 내용이 현재 페이지보다 낮을떄 현재 페이지를 1페이지로 세팅해줌
		if(allPage < pageInfo.getCurPage()) {
			pageInfo.setCurPage(1);
		}
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		System.out.println(allCnt);
		if(noteType.equals("sent")) {
			System.out.println("readType : "+readType);
			noteDtoList = noteDslRepository.findNoteListByUserAndReadTypeAndPaging(username,"sent",sendRead,pageRequest);
		}else { //receive
			System.out.println("receive쪽지 ");
			System.out.println("readType : "+readType);
			noteDtoList = noteDslRepository.findNoteListByUserAndReadTypeAndPaging(username,"receive",sendRead,pageRequest);
		}
		
		//데이터 있는지 없는지 체크
		if(noteDtoList.isEmpty()) {			
			throw new Exception("해당 noteList없음");
		}else {
			return noteDtoList;
		}
	}

	//note자세히
	@Override
	public NoteDto getNoteDtoByNoteNo(Integer noteNo ,String userType) throws Exception {
		Optional<Note> optionalNote = noteRepository.findById(noteNo);
		if(userType != null && userType.equals("receive")) {
			System.out.println("yes로 온다 리시브다 받는경우");
			//userType이 receive라면 읽음 여부 변경
			optionalNote.get().setNoteIsRead("Y");
			noteRepository.save(optionalNote.get());
		}
		if(optionalNote.isEmpty()) throw new Exception("해당번호의 Note없음");
		String sentNick = userRepository.findByUsername(optionalNote.get().getSentUsername()).getUserNickname();
		String receiveNick = userRepository.findByUsername(optionalNote.get().getReceiveUsername()).getUserNickname();
		NoteDto note = NoteDto.builder()
						.noteNo(optionalNote.get().getNoteNo())
						.sentUsername(optionalNote.get().getSentUsername())
			            .sentUserNick(sentNick)
			            .noteContent(optionalNote.get().getNoteContent())
			            .receiveUsername(optionalNote.get().getReceiveUsername())
			            .receiveUserNick(receiveNick)
			            .sentDate(optionalNote.get().getSentDate())
			            .noteIsRead(optionalNote.get().getNoteIsRead())
			            .build();
		return note;
	}
	

}
