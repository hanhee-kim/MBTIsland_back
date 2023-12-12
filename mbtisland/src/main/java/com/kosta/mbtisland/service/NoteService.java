package com.kosta.mbtisland.service;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.entity.Note;

public interface NoteService {
	void noteWrite(NoteDto noteDto) throws Exception;
}
