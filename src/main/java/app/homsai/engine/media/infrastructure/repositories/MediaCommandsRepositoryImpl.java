package app.homsai.engine.media.infrastructure.repositories;

import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.repositories.MediaCommandsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class MediaCommandsRepositoryImpl implements MediaCommandsRepository {

    @Autowired
    MediaCommandsJpaRepository mediaCommandsJpaRepository;

    @Override
    public Media create(Media media) {
        return mediaCommandsJpaRepository.save(media);
    }

    @Override
    public Media update(Media media) {
        return mediaCommandsJpaRepository.save(media);
    }

    @Override
    public void delete(Media media) {
        mediaCommandsJpaRepository.softDelete(media);
    }

}
