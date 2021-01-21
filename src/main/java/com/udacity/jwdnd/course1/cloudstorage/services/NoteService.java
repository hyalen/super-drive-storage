package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

    public Integer insertNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public Integer deleteNote(Integer noteId) {
        return noteMapper.deleteNoteById(noteId);
    }

    public Integer updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }
}
