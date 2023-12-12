package com.kosta.mbtisland.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.mbtisland.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer>{

}
