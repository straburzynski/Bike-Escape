package com.bikeescape.api.controller;

import com.bikeescape.api.service.QrCodeService;
import com.google.zxing.WriterException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/qrCode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @ApiOperation(value = "Generate QR code file from text")
    @PostMapping(value = "/file")
    public ResponseEntity<?> generateQrCodeFile(@RequestParam(value = "text") String text) throws IOException, WriterException {
        qrCodeService.generateQrCodeImageFile(text);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Generate QR code base64 from text")
    @GetMapping(value = "/imageBase64")
    public ResponseEntity<?> generateQrCodeBase64(@RequestParam(value = "text") String text) throws IOException, WriterException {
        String qrCodeBase64 = qrCodeService.generateQrCodeImageBase64(text);
        return new ResponseEntity<>(qrCodeBase64, HttpStatus.OK);
    }

}
