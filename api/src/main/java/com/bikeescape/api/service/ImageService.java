package com.bikeescape.api.service;

import java.io.IOException;

public interface ImageService {

    void saveRaceImage(String raceImage, String raceId) throws IOException;

    byte[] openRaceImage(String raceId) throws IOException;

    byte[] openRaceImageThumbnail(String raceId) throws IOException;

    void deleteRaceImage(String raceId) throws IOException;

    void saveSummaryImage(String summaryImage, String raceId) throws IOException;

    byte[] openSummaryImage(String raceId) throws IOException;

    void deleteSummaryImage(String raceId) throws IOException;

    void saveFailImage(String failImage, String raceId) throws IOException;

    byte[] openFailImage(String raceId) throws IOException;

    void deleteFailImage(String raceId) throws IOException;

    void saveCheckpointImage(String checkpointImage, String raceId, String checkpointId) throws IOException;

    byte[] openCheckpointImage(String raceId, String checkpointId) throws IOException;

    void deleteCheckpointImage(String raceId, String checkpointId) throws IOException;

    void saveUserImage(String userImage, String userId) throws IOException;

    byte[] openUserImage(String userId) throws IOException;

    void deleteUserImage(String userId) throws IOException;

    void deleteRaceImagesFolder(String raceId);

}
