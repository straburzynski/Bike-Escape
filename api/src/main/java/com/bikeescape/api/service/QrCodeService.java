package com.bikeescape.api.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeService {

    byte[] generateQrCodeImage(String text) throws WriterException, IOException;

    void generateQrCodeImageFile(String text) throws WriterException, IOException;

    String generateQrCodeImageBase64(String text) throws WriterException, IOException;

}
