package app.homsai.engine.media.domain.services;

import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.repositories.MediaQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */
@Service
public class MediaQueriesServiceImpl implements MediaQueriesService {

    @Autowired
    MediaQueriesRepository mediaQueriesRepository;

    @Autowired
    StorageService storageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Page<Media> findAll(Pageable pageRequest, String search, String entityUuid) {
        return mediaQueriesRepository.findAll(pageRequest, search, entityUuid);
    }

    @Override
    public Media findOne(String mediaUuid) throws MediaNotFoundException {
        return mediaQueriesRepository.findOne(mediaUuid);
    }

    @Override
    public Resource getFile(String mediaUuid, String res) throws MediaNotFoundException {
        Media media = findOne(mediaUuid);
        String filename = media.getFilename();
        if (res != null) {
            if (res.toUpperCase().equals("FHD"))
                filename += "-FHD";
            if (res.toUpperCase().equals("HD"))
                filename += "-HD";
            if (res.toUpperCase().equals("THUMB"))
                filename += "-thumb";
        }
        return storageService.loadAsResource(filename);
    }

    @Override
    public String getFilePath(String filename) {
        return storageService.load(filename).toAbsolutePath().toString();
    }
}
