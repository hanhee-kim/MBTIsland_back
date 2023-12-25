package com.kosta.mbtisland.service;

import java.util.List;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.dto.PageInfo;
import com.kosta.mbtisland.entity.Note;

public interface NoteService {
	//노트작성
	void noteWrite(NoteDto noteDto) throws Exception;
	//쪽지 불러오기(유저ID,noteType,readType,page)
	List<NoteDto> getNoteListByUsernameAndNoteTypeAndReadTypeAndPage(String username,String noteType,String readType,PageInfo page) throws Exception;
	//쪽지 자세히
	NoteDto getNoteDtoByNoteNo(Integer noteNo , String userType) throws Exception;
	//username으로 읽지않은 노트리스트 5개 가져오기
	List<NoteDto> getNoteListNotReadByUsername(String username) throws Exception;
	//해당 유저의 안읽은 note갯수가져오기
	Long getCntNotReadNoteList(String username) throws Exception;
	//유저한테온 쪽지 모두 읽기
	void allReadNoteByUser(String username) throws Exception;
}
