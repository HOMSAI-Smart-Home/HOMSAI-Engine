package app.homsai.engine.media.application.services;

import app.homsai.engine.media.application.converters.MediaMapper;
import app.homsai.engine.media.application.http.controllers.MediaQueriesController;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.models.MediaEntity;
import app.homsai.engine.media.domain.services.MediaCommandsService;
import app.homsai.engine.media.domain.services.MediaQueriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */


@Service
public class MediaCommandsApplicationServiceImpl implements MediaCommandsApplicationService {

    @Autowired
    MediaCommandsService mediaCommandsService;

    @Autowired
    MediaQueriesService mediaQueriesService;

    @Autowired
    MediaMapper mediaMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public MediaQueriesDto create(MultipartFile file, String uuid, String tag) throws Exception {
        Media media = new Media();

        MediaEntity mediaEntity = new MediaEntity();
        mediaEntity.setMedia(media);
        mediaEntity.setEntityUuid(uuid);

        if (media.getMediaEntity() == null)
            media.setMediaEntity(new HashSet<>());

        media.getMediaEntity().add(mediaEntity);
        media.setCustomTag(tag);

        MediaQueriesDto mediaQueriesDto =
                mediaMapper.convertToDto(mediaCommandsService.create(media, file));
        mediaQueriesDto.add(WebMvcLinkBuilder.linkTo(methodOn(MediaQueriesController.class).getMedia(media.getUuid()))
                .withSelfRel());
        mediaQueriesDto.add(linkTo(
                methodOn(MediaQueriesController.class).getFile(mediaQueriesDto.getUuid(), null))
                        .withRel("download"));

        if (mediaQueriesDto.getTag().equals("image")) {
            mediaQueriesDto.add(linkTo(
                    methodOn(MediaQueriesController.class).getFile(mediaQueriesDto.getUuid(), "HD"))
                            .withRel("download_hd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .getFile(mediaQueriesDto.getUuid(), "FHD")).withRel("download_fhd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .getFile(mediaQueriesDto.getUuid(), "THUMB")).withRel("download_thumb"));
        }

        if (mediaQueriesDto.getTag().equals("video")) {
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .getFile(mediaQueriesDto.getUuid(), "THUMB")).withRel("download_thumb"));
        }

        mediaQueriesDto.add(linkTo(
                methodOn(MediaQueriesController.class).viewMedia(mediaQueriesDto.getUuid(), null))
                        .withRel("view"));

        if (mediaQueriesDto.getTag().equals("image")) {
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "HD")).withRel("view_hd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "FHD")).withRel("view_fhd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "THUMB")).withRel("view_thumb"));
        }

        if (mediaQueriesDto.getTag().equals("video")) {
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "THUMB")).withRel("view_thumb"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class).streamVideo(null,
                    null, mediaQueriesDto.getUuid())).withRel("stream"));
        }

        return mediaQueriesDto;
    }

    @Override
    public void delete(String mediaUuid) throws MediaNotFoundException {
        Media media = mediaQueriesService.findOne(mediaUuid);
        mediaCommandsService.delete(media);
    }

    @Override
    public void associateMediaEntity(String mediaUuid, String entityUuid)
            throws MediaNotFoundException {
        mediaCommandsService.associateMediaEntity(mediaUuid, entityUuid);
    }

    @Override
    public void disassociateMediaEntity(String mediaUuid, String entityUuid)
            throws MediaNotFoundException {
        mediaCommandsService.disassociateMediaEntity(mediaUuid, entityUuid);

    }

    @Override
    public MediaQueriesDto createQRCode(String uuid, String tag) throws Exception {
        Media media = new Media();

        MediaEntity mediaEntity = new MediaEntity();
        mediaEntity.setMedia(media);
        mediaEntity.setEntityUuid(uuid);

        if (media.getMediaEntity() == null)
            media.setMediaEntity(new HashSet<>());

        media.getMediaEntity().add(mediaEntity);
        media.setCustomTag(tag);

        MediaQueriesDto mediaQueriesDto =
                mediaMapper.convertToDto(mediaCommandsService.createQRCode(media));
        mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class).getMedia(media.getUuid()))
                .withSelfRel());
        mediaQueriesDto.add(linkTo(
                methodOn(MediaQueriesController.class).getFile(mediaQueriesDto.getUuid(), null))
                        .withRel("download"));

        if (mediaQueriesDto.getTag().equals("image")) {
            mediaQueriesDto.add(linkTo(
                    methodOn(MediaQueriesController.class).getFile(mediaQueriesDto.getUuid(), "HD"))
                            .withRel("download_hd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .getFile(mediaQueriesDto.getUuid(), "FHD")).withRel("download_fhd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .getFile(mediaQueriesDto.getUuid(), "THUMB")).withRel("download_thumb"));
        }

        mediaQueriesDto.add(linkTo(
                methodOn(MediaQueriesController.class).viewMedia(mediaQueriesDto.getUuid(), null))
                        .withRel("view"));

        if (mediaQueriesDto.getTag().equals("image")) {
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "HD")).withRel("view_hd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "FHD")).withRel("view_fhd"));
            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), "THUMB")).withRel("view_thumb"));
        }

        return mediaQueriesDto;
    }
}
