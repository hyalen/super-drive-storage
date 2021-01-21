package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/upload-note")
    public String uploadNote(Authentication auth, Model model, @ModelAttribute("noteForm") Note note) {
        String errorMsg = null;
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Note storedUser = noteService.getNoteById(note.getNoteId());

        if (storedUser != null) {
            try {
                int noteId = noteService.updateNote(note);

                if (noteId < 0) {
                    errorMsg = "There was an error uploading this note.";
                }
            } catch (Exception e) {
                System.out.println(e);
                errorMsg = "There was an error uploading this note.";
            }
        } else {
            note.setUserId(userId);
            int insertedNote = noteService.insertNote(note);

            if (insertedNote < 0) {
                errorMsg = "There was an error uploading this note.";
            }
        }

        if (errorMsg == null) {
            model.addAttribute("successOperation", true);
        } else {
            model.addAttribute("errorOperation", errorMsg);
        }

        return "result";
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable(name = "noteId") int noteId, Model model) {
        String errorMsg = null;
        int deletedNote = noteService.deleteNote(noteId);

        if (deletedNote < 0) {
            errorMsg = "There was an error uploading this note.";
        }

        if (errorMsg == null) {
            model.addAttribute("successOperation", true);
        } else {
            model.addAttribute("errorOperation", errorMsg);
        }

        return "result";
    }
}
