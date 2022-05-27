package app.homsai.engine.media.domain.services;

import app.homsai.engine.media.domain.exceptions.MediaNotSupportedException;
import app.homsai.engine.media.domain.repositories.MediaCommandsRepository;
import com.google.zxing.WriterException;
import app.homsai.engine.common.domain.utils.MimeTypeHelper;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.models.MediaEntity;
import app.homsai.engine.media.domain.repositories.MediaQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */
@Service
public class MediaCommandsServiceImpl implements MediaCommandsService {

    @Autowired
    MediaCommandsRepository mediaCommandsRepository;

    @Autowired
    MediaQueriesRepository mediaQueriesRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    ImageService imageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Media create(Media media, MultipartFile file) throws MediaNotSupportedException {
        String mimeType = file.getContentType();

        if (!MimeTypeHelper.isSupportedMimeTypes(mimeType)) {
            throw new MediaNotSupportedException(mimeType);
        }

        String newFileName = String.valueOf(UUID.randomUUID());
        String oldFileName = file.getOriginalFilename();
        String extension = file.getOriginalFilename().substring(
                file.getOriginalFilename().length() - 4, file.getOriginalFilename().length());

        if (extension.contains(".")) {
            extension = extension.split("\\.")[1];
        }

        if (mimeType.contains("image")) {
            int type = imageService.generateScaledImages(file, newFileName, extension);
            media.setType(type);
        }

        storageService.store(file, newFileName);

        media.setMimetype(file.getContentType());
        media.setFilename(newFileName);
        media.setOriginalFileName(oldFileName);
        media.setTag(file.getContentType().split("\\/")[0]);
        media.setSize(file.getSize());
        media.setOriginalExtension(extension);

        Media createdMedia = mediaCommandsRepository.create(media);
        return createdMedia;
    }

    @Override
    public void delete(Media media) {
        mediaCommandsRepository.delete(media);
    }

    @Override
    public void disassociateMediaEntity(String mediaUuid, String entityUuid)
            throws MediaNotFoundException {
        Media media = mediaQueriesRepository.findOne(mediaUuid);
        Set<MediaEntity> mediaEntities = media.getMediaEntity();
        MediaEntity mediaEntityToRemove = null;

        for (MediaEntity mediaEntity : mediaEntities) {
            if (mediaEntity.getEntityUuid().equals(entityUuid)) {
                mediaEntityToRemove = mediaEntity;
                break;
            }
        }

        if (mediaEntityToRemove != null) {
            media.getMediaEntity().remove(mediaEntityToRemove);
            storageService.deleteFile(media.getFilename());
            mediaCommandsRepository.update(media);
        }
    }

    @Override
    public void associateMediaEntity(String mediaUuid, String entityUuid)
            throws MediaNotFoundException {
        Media media = mediaQueriesRepository.findOne(mediaUuid);

        MediaEntity mediaEntity = new MediaEntity();
        mediaEntity.setMedia(media);
        mediaEntity.setEntityUuid(entityUuid);

        if (media.getMediaEntity() == null)
            media.setMediaEntity(new HashSet<>());

        media.getMediaEntity().add(mediaEntity);

        mediaCommandsRepository.update(media);
    }

    @Override
    public Media createQRCode(Media media) throws WriterException {
        String newFileName = String.valueOf(UUID.randomUUID());
        String extension = "png";

        BufferedImage bufferedImage =
                imageService.generateQRCode(newFileName, 1280, 1280, newFileName);
        imageService.generateScaledImages(bufferedImage, newFileName, extension);

        media.setMimetype("image/png");
        media.setFilename(newFileName);
        media.setOriginalFileName(newFileName);
        media.setTag("image");
        media.setSize(1000L);
        media.setOriginalExtension(extension);

        return mediaCommandsRepository.create(media);
    }
}
