package app.homsai.engine.media.infrastructure.repositories;

import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.models.MediaEntity;
import app.homsai.engine.media.domain.repositories.MediaQueriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

/**
 * Created by Giacomo Agostini on 17/01/17.
 */

@Repository
public class MediaQueriesRepositoryImpl implements MediaQueriesRepository {

    @Autowired
    MediaQueriesJpaRepository mediaQueriesJpaRepository;

    @Autowired
    MediaEntityQueriesJpaRepository mediaEntityQueriesJpaRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Page<Media> findAll(Pageable pageRequest, String search, String entityUuid) {
        MediaHasEntityUuid mediaHasEntityUuidSpecification = null;
        if (entityUuid != null)
            mediaHasEntityUuidSpecification = new MediaHasEntityUuid(entityUuid);
        return mediaQueriesJpaRepository.findAllActive(pageRequest, search,
                mediaHasEntityUuidSpecification);
    }

    @Override
    public Media findOne(String mediaUuid) throws MediaNotFoundException {
        Media media = mediaQueriesJpaRepository.findOneActive(mediaUuid);
        if (media == null) {
            throw new MediaNotFoundException(mediaUuid);
        }
        return media;

    }

    private static final class MediaHasEntityUuid implements Specification<Media> {

        private String entityUuid;

        public MediaHasEntityUuid(String entityUuid) {
            this.entityUuid = entityUuid;
        }

        @Override
        public Predicate toPredicate(Root<Media> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Join<Media, MediaEntity> join = root.join("mediaEntity");
            return cb.equal(join.get("entityUuid"), entityUuid);
        }
    }
}
