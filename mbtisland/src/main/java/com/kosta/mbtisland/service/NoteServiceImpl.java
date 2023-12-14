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
		//노트 작성하면 알림테이블에 추가
//		System.out.println(note.getNoteNo());
//		Alarm alarm = Alarm.builder()
//				.username(note.getReceiveUsername())
//				.alarmType("쪽지")
//				.alarmTargetNo(note.getNoteNo())
//				.alarmTargetFrom("Note")
//				.alarmReadDate(null)
//				.alarmUpdateDtae(null)
//				.build();
//		alarmRepository.save(alarm);
	//readDate랑 updateDate 왜 NOT NULL	?		
	
		
	}
	
	//쪽지 불러오기(유저ID,noteType,readType,page)
	@Override
	public List<NoteDto> getNoteListByUsernameAndNoteTypeAndReadTypeAndPage(String username, String noteType,
			String readType, PageInfo pageInfo) throws Exception {
		Integer itemsPerPage = 10;
		int pagesPerGroup = 10;
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, itemsPerPage);
		List<NoteDto> noteDtoList = new ArrayList<>();
		if(noteType.equals("sent")) {
			System.out.println("readType : "+readType);
			if(readType == null ||readType.equals("") || readType.equals("null")) {
				System.out.println("all");
				noteDtoList = noteDslRepository.findNoteListByUserAndNoteTypeAndPaging(username,"sent",pageRequest);
			}else { //N
				noteDtoList = noteDslRepository.findNoteListByUserAndReadTypeAndPaging(username,"sent",readType,pageRequest);
			}
		}else { //receive
			System.out.println("receive쪽지 ");
			System.out.println("readType : "+readType);
			if(readType == null ||readType.equals("") || readType.equals("null")) {
				noteDtoList = noteDslRepository.findNoteListByUserAndNoteTypeAndPaging(username,"receive",pageRequest);
			}else { //N
				noteDtoList = noteDslRepository.findNoteListByUserAndReadTypeAndPaging(username,"receive","N",pageRequest);
			}
		}
		Integer allCount = noteDtoList.size();
		Integer allPage = (int) Math.ceil((double) allCount / itemsPerPage);
		Integer startPage = (int) ((pageInfo.getCurPage() - 1) / pagesPerGroup) * pagesPerGroup + 1;
		Integer endPage = Math.min(startPage + pagesPerGroup - 1, allPage);
		if(endPage>allPage) endPage = allPage;
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
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
