package app.homsai.engine.media.domain.repositories;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

import app.homsai.engine.media.domain.models.Media;

public interface MediaCommandsRepository {
    Media create(Media media);

    Media update(Media media);

    void delete(Media media);
}
