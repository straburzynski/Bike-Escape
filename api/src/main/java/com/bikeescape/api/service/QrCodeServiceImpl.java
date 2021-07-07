package com.bikeescape.api.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    private final static String QRCODES_FOLDER = "qrcodes/";
    private final static int HEIGHT = 500;
    private final static int WIDTH = 500;
    private final Environment env;

    @Autowired
    public QrCodeServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public byte[] generateQrCodeImage(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, getQrCodeOptions());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", os);
        return os.toByteArray();
    }

    @Override
    public void generateQrCodeImageFile(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, getQrCodeOptions());
        String filepath = env.getProperty("path.upload.files") + QRCODES_FOLDER + text + ".png";
        Path path = Paths.get(filepath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    @Override
    public String generateQrCodeImageBase64(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, getQrCodeOptions());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", os);
        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    private Map<EncodeHintType, String> getQrCodeOptions() {
        Map<EncodeHintType, String> options = new HashMap<>();
        options.put(EncodeHintType.CHARACTER_SET, "utf-8");
        return options;
    }

}
