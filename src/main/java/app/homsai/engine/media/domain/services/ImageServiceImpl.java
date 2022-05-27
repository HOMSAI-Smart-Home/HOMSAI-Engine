package app.homsai.engine.media.domain.services;

import app.homsai.engine.media.domain.exceptions.StorageException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Giacomo Agostini on 18/01/2017.
 */

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    StorageService storageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public int generateScaledImages(MultipartFile multipartFile, String fileName,
            String extension) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(multipartFile.getBytes());
            BufferedImage bufferedImage = ImageIO.read(bis);
            BufferedImage scaledImage1 = scaleImage(bufferedImage, 300);
            BufferedImage scaledImage2 = scaleImage(bufferedImage, 1280);
            BufferedImage scaledImage3 = scaleImage(bufferedImage, 1920);
            storageService.store(scaledImage1, fileName + "-thumb", extension);
            storageService.store(scaledImage2, fileName + "-HD", extension);
            storageService.store(scaledImage3, fileName + "-FHD", extension);
            return bufferedImage.getType();
        } catch (IOException e) {
            throw new StorageException("Failed to resize images");
        }
    }

    @Override
    public int generateScaledImages(BufferedImage bufferedImage, String fileName,
            String extension) {
        BufferedImage scaledImage1 = scaleImage(bufferedImage, 300);
        BufferedImage scaledImage2 = scaleImage(bufferedImage, 1280);
        BufferedImage scaledImage3 = scaleImage(bufferedImage, 1920);
        storageService.store(scaledImage1, fileName + "-thumb", extension);
        storageService.store(scaledImage2, fileName + "-HD", extension);
        storageService.store(scaledImage3, fileName + "-FHD", extension);
        return bufferedImage.getType();
    }



    @Override
    public BufferedImage scaleImage(final BufferedImage bufferedImage, final int size) {
        int type = bufferedImage.getType();
        final double boundSize = size;
        final int origWidth = bufferedImage.getWidth();
        final int origHeight = bufferedImage.getHeight();

        double scale;

        if (origHeight > origWidth)
            scale = boundSize / origHeight;
        else
            scale = boundSize / origWidth;

        /*
         * Don't scale up small images.
         */
        if (scale > 1.0)
            return (bufferedImage);

        final int scaledWidth = (int) (scale * origWidth);
        final int scaledHeight = (int) (scale * origHeight);

        final Image scaledImage =
                bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // new ImageIcon(image); // load image
        // scaledWidth = scaledImage.getWidth(null);
        // scaledHeight = scaledImage.getHeight(null);

        final BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, type);

        final Graphics2D g = scaledBI.createGraphics();

        g.drawImage(scaledImage, 0, 0, null);

        g.dispose();

        return (scaledBI);
    }


    @Override
    public BufferedImage generateQRCode(String fileName, int width, int height, String content)
            throws WriterException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // create
                                                                                            // an
                                                                                            // empty
                                                                                            // image
        int white = 255 << 16 | 255 << 8 | 255;
        int black = 0;
        BitMatrix matrix =
                new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, matrix.get(i, j) ? black : white); // set pixel one by one
            }
        }
        storageService.store(image, fileName, "png");
        return image;
    }
}
