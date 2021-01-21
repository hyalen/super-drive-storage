package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getFilesForUser(userId);
    }

    public int insert(MultipartFile multipartFile, int userId) throws IOException {
        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), userId, multipartFile.getBytes());
        return fileMapper.insertFile(file);
    }

    public Integer deleteFileById(Integer fileId) {
        return fileMapper.deleteFileById(fileId);
    }

    public boolean isFilenameAvailable(String fileName) {
        return fileMapper.getFileByFilename(fileName) == null;
    }
}
