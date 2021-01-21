package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    // setting new DTO instances on every user request
    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    @PostMapping("/upload-file")
    public String uploadFile(Authentication auth, Model model, @ModelAttribute("fileDTO") MultipartFile file) throws IOException  {
        String errorMsg = null;
        User user = userService.getUser(auth.getName());

        if (file.isEmpty()) {
            errorMsg = "You haven't selected any files yet. Please choose one.";
        } else if (!fileService.isFilenameAvailable(file.getOriginalFilename())) {
            errorMsg = "Two or more files with the same name are not allowed.";
        }

        if (errorMsg == null) {
            int fileId = fileService.insert(file, user.getUserId());

            if (fileId < 0) {
                errorMsg = "There was an error uploading this file.";
            }
        }

        if (errorMsg == null) {
            model.addAttribute("successOperation", true);
        } else {
            model.addAttribute("errorOperation", errorMsg);
        }

        return "result";
    }

    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(@PathVariable(name = "fileId") int fileId, Model model) {
        String errorMsg = null;
        int deletedFile = fileService.deleteFileById(fileId);

        if (deletedFile < 0) {
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
