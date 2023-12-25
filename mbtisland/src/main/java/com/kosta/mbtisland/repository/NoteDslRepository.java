package com.kosta.mbtisland.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.entity.Note;
import com.kosta.mbtisland.entity.QNote;
import com.kosta.mbtisland.entity.QUserEntity;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.kosta.mbtisland.entity.QNote.note;
import static com.kosta.mbtisland.entity.QQuestion.question;
import static com.kosta.mbtisland.entity.QUserEntity.userEntity;

@Repository
public class NoteDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryfactory;
	
	//Tuple > NoteDto로 변환
	public List<NoteDto> changeNoteDto(List<Tuple> result){
		List<NoteDto> noteDtoList = new ArrayList<>();
		for (Tuple tuple : result) {
			Note noteClass = tuple.get(0, Note.class);
			String sentNick = tuple.get(1,String.class);
			String receiveNick = tuple.get(2,String.class);
//			System.out.println("sentNick : "+sentNick);
		    noteDtoList.add(NoteDto.builder()
		            .noteNo(noteClass.getNoteNo())
		            .sentUsername(noteClass.getSentUsername())
		            .sentUserNick(sentNick)
		            .noteContent(noteClass.getNoteContent())
		            .receiveUsername(noteClass.getReceiveUsername())
		            .receiveUserNick(receiveNick)
		            .sentDate(noteClass.getSentDate())
		            .noteIsRead(noteClass.getNoteIsRead())
		            .build());
		}
		return noteDtoList;
	}
	
	public List<NoteDto> findNoteListByUserAndReadTypeAndPaging(String username,String noteType,String noteIsRead,PageRequest pageRequest){
		System.out.println("dsl쿼리문진입");
		QNote note = QNote.note;
		QUserEntity sentUser = new QUserEntity("sentUser");
		QUserEntity receiveUser = new QUserEntity("receiveUser");
		List<Tuple> result = new ArrayList<Tuple>();
		if(noteType.equals("sent")) {
			// 'sentUsername'
			result = jpaQueryfactory
					.select(note, sentUser.userNickname.as("sentUserNickname"), receiveUser.userNickname.as("receiveUserNickname"))
					.from(note)
					.leftJoin(sentUser).on(note.sentUsername.eq(sentUser.username))
					.leftJoin(receiveUser).on(note.receiveUsername.eq(receiveUser.username))
					.where(note.sentUsername.eq(username).and(
							noteIsRead!=null?note.noteIsRead.eq(noteIsRead):null)
							)
//					.orderBy(note.noteIsRead.asc())
					.orderBy(note.noteNo.desc())	//번호로 정렬하려면
					.offset(pageRequest.getOffset()) // 시작행의 위치
					.limit(pageRequest.getPageSize()) // 페이지당 항목 수
					.fetch();
		}else {
			result = jpaQueryfactory
			        .select(note, sentUser.userNickname.as("sentUserNickname"), receiveUser.userNickname.as("receiveUserNickname"))
			        .from(note)
			        .leftJoin(sentUser).on(note.sentUsername.eq(sentUser.username))
			        .leftJoin(receiveUser).on(note.receiveUsername.eq(receiveUser.username))
			        .where(note.receiveUsername.eq(username).and(
			        		noteIsRead!=null?note.noteIsRead.eq(noteIsRead):null)
			        		)
			        .orderBy(note.noteNo.desc())
			        .offset(pageRequest.getOffset()) // 시작행의 위치
					.limit(pageRequest.getPageSize()) // 페이지당 항목 수
			        .fetch();
		}

		return changeNoteDto(result);
		
	}
	
	public List<NoteDto> findNoteListByUserAndNoteTypeAndPaging(String username,String noteType ,PageRequest pageRequest){
		QNote note = QNote.note;
		QUserEntity sentUser = new QUserEntity("sentUser");
		QUserEntity receiveUser = new QUserEntity("receiveUser");
		List<Tuple> result = new ArrayList<Tuple>();
		if(noteType.equals("sent")) {
			result = jpaQueryfactory
					.select(note, sentUser.userNickname.as("sentUserNickname"), receiveUser.userNickname.as("receiveUserNickname"))
					.from(note)
					.leftJoin(sentUser).on(note.sentUsername.eq(sentUser.username))
					.leftJoin(receiveUser).on(note.receiveUsername.eq(receiveUser.username))
					.where(note.sentUsername.eq(username))
					.orderBy(note.noteIsRead.asc())
					.offset(pageRequest.getOffset()) // 시작행의 위치
					.limit(pageRequest.getPageSize()) // 페이지당 항목 수
					.fetch();
			
		}else {
			result = jpaQueryfactory
					.select(note, sentUser.userNickname.as("sentUserNickname"), receiveUser.userNickname.as("receiveUserNickname"))
					.from(note)
					.leftJoin(sentUser).on(note.sentUsername.eq(sentUser.username))
					.leftJoin(receiveUser).on(note.receiveUsername.eq(receiveUser.username))
					.where(note.receiveUsername.eq(username))
					.orderBy(note.noteIsRead.asc())
					.offset(pageRequest.getOffset()) // 시작행의 위치
					.limit(pageRequest.getPageSize()) // 페이지당 항목 수
					.fetch();
		}
		// 
		return changeNoteDto(result);
	}

	public Long findNoteCntByUserAndNoteTypeAndReadType(String username,String noteType,String readType) {
		System.out.println("dsl:"+username+noteType+readType);
		if(noteType.equals("sent")) {
			return jpaQueryfactory
					.select(note.count())
					.from(note)
					.where(note.sentUsername.eq(username).and(readType!=null?note.noteIsRead.eq(readType):null))
					.fetchOne();
		}
		else {
			return jpaQueryfactory
					.select(note.count())
					.from(note)
					.where(note.receiveUsername.eq(username).and(readType!=null?note.noteIsRead.eq(readType):null))
					.fetchOne();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
