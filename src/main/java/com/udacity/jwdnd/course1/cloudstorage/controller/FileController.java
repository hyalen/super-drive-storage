package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            return "error";
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

    @GetMapping("/download-file")
    public ResponseEntity downloadFile(Authentication auth, @RequestParam String fileName, Model model) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        List<File> files = fileService.getFiles(userId);
        File newFile = files.get(0);

        return ResponseEntity
            .ok()
            .header("Content-Type: application/force-download")
            .header("Content-type: application/pdf")
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newFile.getFileName() + "\"")
            .body(newFile);
    }
}
