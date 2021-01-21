package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;

    private List<File> fileList;
    private List<Note> noteList;
    private List<Credential> credentialList;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @PostConstruct
    public void postConstruct() {
        fileList = new ArrayList<>();
        noteList = new ArrayList<>();
        credentialList = new ArrayList<>();
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    @ModelAttribute("noteForm")
    public Note getNoteForm() {
        return new Note();
    }

    @ModelAttribute("credentialForm")
    public Credential getCredentialForm() {
        return new Credential();
    }

    @GetMapping()
    public String homeView(Authentication auth, Model model) throws Exception {
        Integer userId = userService.getUser(auth.getName()).getUserId();

        // getting all of the records from the DB and appending to its respective lists
        fileList = fileService.getFiles(userId);
        noteList = noteService.getNotes(userId);
        credentialList = credentialService.getCredentials(userId);

        // appending to map variables, so that they can be available at the templates
        model.addAttribute("fileList", fileList);
        model.addAttribute("noteList", noteList);
        model.addAttribute("credentialList", credentialList);
        return "home";
    }
}
