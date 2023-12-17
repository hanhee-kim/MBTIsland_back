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
	NoteDto getNoteDtoByNoteNo(Integer noteNo , String userType)throws Exception;
}
