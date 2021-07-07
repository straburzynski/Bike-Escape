package com.bikeescape.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {

    void saveFile(MultipartFile file, String folder, String filename) throws IOException;

    void saveFile(String file, String folder, String filename) throws IOException;

    void deleteFile(String folder, String filename) throws IOException;

    byte[] openFile(String folder, String filename) throws IOException;

    File getFile(String folder, String filename) throws IOException;

    void deleteFolder(String folder);
}