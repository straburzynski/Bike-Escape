package com.bikeescape.api.controller;

import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserImageTO;
import com.bikeescape.api.security.AuthService;
import com.bikeescape.api.service.ImageService;
import com.bikeescape.api.service.QrCodeService;
import com.google.zxing.WriterException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log
@CrossOrigin
@RestController
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;
    private final AuthService authService;
    private final QrCodeService qrCodeService;

    @Autowired
    public ImageController(ImageService imageService, AuthService authService, QrCodeService qrCodeService) {
        this.imageService = imageService;
        this.authService = authService;
        this.qrCodeService = qrCodeService;
    }

    // ------------- race image -------------

    @ApiOperation(value = "Get race image")
    @GetMapping(value = "/raceImage", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getRaceImage(@RequestParam String raceId) throws IOException {
        return imageService.openRaceImage(raceId);
    }

    // ------------- race thumbnail image -------------

    @ApiOperation(value = "Get race image thumbnail")
    @GetMapping(value = "/raceImageThumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getRaceImageThumbnail(@RequestParam String raceId) throws IOException {
        return imageService.openRaceImageThumbnail(raceId);
    }

    // ------------- summary image -------------

    @ApiOperation(value = "Get summary image")
    @GetMapping(value = "/summaryImage", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getSummaryImage(@RequestParam String raceId) throws IOException {
        return imageService.openSummaryImage(raceId);
    }

    // ------------- fail image -------------

    @ApiOperation(value = "Get fail image")
    @GetMapping(value = "/failImage", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getFailImage(@RequestParam String raceId) throws IOException {
        return imageService.openFailImage(raceId);
    }

    // ------------- checkpoint image -------------

    @ApiOperation(value = "Get checkpoint image")
    @GetMapping(value = "/checkpointImage", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getCheckpointImage(@RequestParam String raceId,
                                     @RequestParam String checkpointId) throws IOException {
        return imageService.openCheckpointImage(raceId, checkpointId);
    }

    // ------------- user image -------------

    @ApiOperation(value = "Upload user image")
    @PostMapping("/userImage")
    public ResponseEntity<?> uploadUserImage(@RequestBody UserImageTO userImageTO,
                                             HttpServletRequest request) throws IOException {
        User user = authService.getLoggedUser(request);
        imageService.saveUserImage(userImageTO.getUserImage(), user.getId().toString());
        log.info("Avatar updated for user: " + user.toString());
        return ResponseEntity.ok("User image saved");
    }

    @ApiOperation(value = "Delete user image")
    @DeleteMapping("/userImage")
    public ResponseEntity<?> deleteUserImage(HttpServletRequest request) throws IOException {
        User user = authService.getLoggedUser(request);
        imageService.deleteUserImage(user.getId().toString());
        log.info("Avatar deleted for user: " + user.toString());
        return ResponseEntity.ok("User image deleted");
    }

    @ApiOperation(value = "Get user image")
    @GetMapping(value = "/userImage", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getUserImage(@RequestParam String userId, HttpServletRequest request) throws IOException {
        if (userId == null) {
            User user = authService.getLoggedUser(request);
            userId = user.getId().toString();
        }
        return imageService.openUserImage(userId);
    }

    // ------------- QR code image -------------

    @ApiOperation(value = "Generate QR code image from text")
    @GetMapping(value = "/qrCode", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getQrCode(@RequestParam(value = "text") String text) throws IOException, WriterException {
        return qrCodeService.generateQrCodeImage(text);
    }

}
