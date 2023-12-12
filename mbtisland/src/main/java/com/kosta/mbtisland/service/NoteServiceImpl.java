package com.kosta.mbtisland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.mbtisland.dto.NoteDto;
import com.kosta.mbtisland.entity.Note;
import com.kosta.mbtisland.repository.NoteDslRepository;
import com.kosta.mbtisland.repository.NoteRepository;

@Service
public class NoteServiceImpl implements NoteService{
	
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private NoteDslRepository noteDslRepository;

	@Override
	public void noteWrite(NoteDto noteDto) throws Exception {
		Note note = Note.builder()
						.sentUsername(noteDto.getSentUsername())
						.noteContent(noteDto.getNoteContent())
						.receiveUsername(noteDto.getReceiveUsername())
						.build();
		noteRepository.save(note);
				
	}

}
