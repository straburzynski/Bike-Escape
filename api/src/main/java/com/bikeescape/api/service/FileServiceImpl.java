package com.bikeescape.api.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log
@Service
public class FileServiceImpl implements FileService {

    private final Environment env;

    @Autowired
    public FileServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public void saveFile(MultipartFile file, String folder, String filename) {
        log.info("File saved: " + folder + filename);
        if (!file.isEmpty()) {
            try {
                String filepath = folder + "/" + filename;
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception ex) {
                System.out.println("Error saving file: " + ex);
            }
        } else {
            System.out.println("File is empty: " + filename);
        }
    }

    @Override
    public void saveFile(String file, String folder, String filename) {
        log.info("File saved: " + folder + filename);
        try {
            createFolderIfNotExists(folder);
            String filepath = folder + "/" + filename;
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            String base64Image = file.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            stream.write(imageBytes);
            stream.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteFile(String folder, String filename) {
        log.info("File deleted: " + folder + filename);
        String filepath = folder + "/" + filename;
        File file = new File(filepath);
        if (file.exists() && !file.isDirectory()) {
            if (!file.delete()) log.warning("Error deleting file: " + filepath);
        }
    }

    @Override
    public void deleteFolder(String folder) {
        log.info("Folder deleted: " + folder);
        File file = new File(folder);
        if (!FileSystemUtils.deleteRecursively(file)) {
            log.warning("Error deleting folder: " + folder);
        }
    }

    @Override
    public byte[] openFile(String folder, String filename) throws IOException {
        String filepath = folder + "/" + filename;
        File file = new File(filepath);
        if (file.exists() && !file.isDirectory()) {
            return Files.readAllBytes(Paths.get(filepath));
        } else {
            String noRaceImagePath = env.getProperty("path.upload.files") + "images/no-image.jpg";
            return Files.readAllBytes(Paths.get(noRaceImagePath));
        }
    }

    @Override
    public File getFile(String folder, String filename) {
        String filepath = folder + "/" + filename;
        File file = new File(filepath);
        if (file.exists() && !file.isDirectory()) {
            return file;
        } else {
            String noRaceImagePath = env.getProperty("path.upload.files") + "images/no-image.jpg";
            return new File(noRaceImagePath);
        }
    }

    private void createFolderIfNotExists(String dirName) throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            if (!theDir.mkdir()) log.warning("Error creating folder: " + dirName);
        }
    }

}
