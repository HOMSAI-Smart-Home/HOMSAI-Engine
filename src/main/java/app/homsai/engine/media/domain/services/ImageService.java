package app.homsai.engine.media.domain.services;

import com.google.zxing.WriterException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface ImageService {

    int generateScaledImages(MultipartFile multipartFile, String fileName, String extension);

    int generateScaledImages(BufferedImage bufferedImage, String fileName, String extension);

    BufferedImage scaleImage(BufferedImage bufferedImage, int size);


    BufferedImage generateQRCode(String fileName, int width, int height, String content)
            throws WriterException;
}
