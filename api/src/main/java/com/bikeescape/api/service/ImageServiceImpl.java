package com.bikeescape.api.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Log
@Service
public class ImageServiceImpl implements ImageService {

    private final String IMAGES_FOLDER = "images/";
    private final String AVATARS_FOLDER = "avatars/";

    private final String RACE_IMAGE = "-race_image.jpg";
    private final String SUMMARY_IMAGE = "-race_summary.jpg";
    private final String FAIL_IMAGE = "-race_fail.jpg";
    private final String CHECKPOINT_IMAGE = "-checkpoint_image.jpg";
    private final String USER_IMAGE = "-user_image.jpg";

    private final FileService fileService;
    private final Environment env;

    @Autowired
    public ImageServiceImpl(FileService fileService, Environment env) {
        this.fileService = fileService;
        this.env = env;
    }

    // ----- race image -----

    @Override
    public void saveRaceImage(String raceImage, String raceId) throws IOException {
        fileService.saveFile(raceImage, getRaceImageFolder(raceId), raceId + RACE_IMAGE);
    }

    @Override
    public byte[] openRaceImage(String raceId) throws IOException {
        return fileService.openFile(getRaceImageFolder(raceId), raceId + RACE_IMAGE);
    }

    @Override
    public byte[] openRaceImageThumbnail(String raceId) throws IOException {
        File file = fileService.getFile(getRaceImageFolder(raceId), raceId + RACE_IMAGE);
        BufferedImage image = ImageIO.read(file);
        BufferedImage resized = resize(image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resized, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    @Override
    public void deleteRaceImage(String raceId) throws IOException {
        fileService.deleteFile(getRaceImageFolder(raceId), raceId + RACE_IMAGE);
    }

    // ----- race summary image -----

    @Override
    public void saveSummaryImage(String summaryImage, String raceId) throws IOException {
        fileService.saveFile(summaryImage, getRaceImageFolder(raceId), raceId + SUMMARY_IMAGE);
    }

    @Override
    public byte[] openSummaryImage(String raceId) throws IOException {
        return fileService.openFile(getRaceImageFolder(raceId), raceId + SUMMARY_IMAGE);
    }

    @Override
    public void deleteSummaryImage(String raceId) throws IOException {
        fileService.deleteFile(getRaceImageFolder(raceId), raceId + SUMMARY_IMAGE);
    }

    // ----- race fail image -----

    @Override
    public void saveFailImage(String failImage, String raceId) throws IOException {
        fileService.saveFile(failImage, getRaceImageFolder(raceId), raceId + FAIL_IMAGE);
    }

    @Override
    public byte[] openFailImage(String raceId) throws IOException {
        return fileService.openFile(getRaceImageFolder(raceId), raceId + FAIL_IMAGE);
    }

    @Override
    public void deleteFailImage(String raceId) throws IOException {
        fileService.deleteFile(getRaceImageFolder(raceId), raceId + FAIL_IMAGE);
    }

    // ----- race checkpoint image -----

    @Override
    public void saveCheckpointImage(String checkpointImage, String raceId, String checkpointId) throws IOException {
        String filename = raceId + "-" + checkpointId + CHECKPOINT_IMAGE;
        fileService.saveFile(checkpointImage, getRaceImageFolder(raceId), filename);
    }

    @Override
    public byte[] openCheckpointImage(String raceId, String checkpointId) throws IOException {
        String filename = raceId + "-" + checkpointId + CHECKPOINT_IMAGE;
        return fileService.openFile(getRaceImageFolder(raceId), filename);
    }

    @Override
    public void deleteCheckpointImage(String raceId, String checkpointId) throws IOException {
        String filename = raceId + "-" + checkpointId + CHECKPOINT_IMAGE;
        fileService.deleteFile(getRaceImageFolder(raceId), filename);
    }

    // ----- avatar image -----

    @Override
    public void saveUserImage(String userImage, String userId) throws IOException {
        log.info("Avatar saved for user id " + userId );
        String filename = userId + USER_IMAGE;
        fileService.saveFile(userImage, getAvatarImageFolder(), filename);
    }

    @Override
    public byte[] openUserImage(String userId) throws IOException {
        String filename = userId + USER_IMAGE;
        return fileService.openFile(getAvatarImageFolder(), filename);
    }

    @Override
    public void deleteUserImage(String userId) throws IOException {
        String filename = userId + USER_IMAGE;
        fileService.deleteFile(getAvatarImageFolder(), filename);
    }

    // ----- race image folder -----

    @Override
    public void deleteRaceImagesFolder(String raceId) {
        fileService.deleteFolder(getRaceImageFolder(raceId));
    }

    // ----- helpers -----

    private static BufferedImage resize(BufferedImage img) {
        Image tmp = img.getScaledInstance(120, 80, Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(120, 80, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    private String getBasePath() {
        return env.getProperty("path.upload.files");
    }

    private String getRaceImageFolder(String raceId) {
        return getBasePath() + IMAGES_FOLDER + raceId;
    }

    private String getAvatarImageFolder() {
        return getBasePath() + AVATARS_FOLDER;
    }

}