package app.homsai.engine.media.application.services;

import app.homsai.engine.media.application.converters.MediaMapper;
import app.homsai.engine.media.application.http.controllers.MediaQueriesController;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.models.Media;
import app.homsai.engine.media.domain.services.MediaQueriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Created by Giacomo Agostini on 02/12/16.
 */

@Service
public class MediaQueriesApplicationServiceImpl implements MediaQueriesApplicationService {

    @Autowired
    MediaQueriesService mediaQueriesService;

    @Autowired
    MediaMapper mediaMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Page<MediaQueriesDto> findAll(Pageable pageRequest, String search, String entityUuid)
            throws Exception {
        Page<Media> medias = mediaQueriesService.findAll(pageRequest, search, entityUuid);
        List<MediaQueriesDto> mediaQueriesDtos = new ArrayList<>();

        for (Media media : medias) {
            MediaQueriesDto mediaQueriesDto = mediaMapper.convertToDto(media);
            mediaQueriesDto
                    .setFilePath(mediaQueriesService.getFilePath(mediaQueriesDto.getFilename()));

            if (media.getMediaEntity() != null && !media.getMediaEntity().isEmpty()) {
                mediaQueriesDto
                        .setEntityUuid(media.getMediaEntity().iterator().next().getEntityUuid());
            }

            mediaQueriesDto.add(WebMvcLinkBuilder.linkTo(
                    methodOn(MediaQueriesController.class).getMedia(mediaQueriesDto.getUuid()))
                            .withSelfRel());
            mediaQueriesDto.add(linkTo(
                    methodOn(MediaQueriesController.class).getFile(mediaQueriesDto.getUuid(), null))
                            .withRel("download"));

            if (mediaQueriesDto.getTag().equals("image")) {
                mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                        .getFile(mediaQueriesDto.getUuid(), "HD")).withRel("download_hd"));
                mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                        .getFile(mediaQueriesDto.getUuid(), "FHD")).withRel("download_fhd"));
                mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                        .getFile(mediaQueriesDto.getUuid(), "THUMB")).withRel("download_thumb"));
            }

            if (mediaQueriesDto.getTag().equals("video")) {
                mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                        .getFile(mediaQueriesDto.getUuid(), "THUMB")).withRel("download_thumb"));
            }

            mediaQueriesDto.add(linkTo(methodOn(MediaQueriesController.class)
                    .viewMedia(mediaQueriesDto.getUuid(), null)).withRel("view"));

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

            mediaQueriesDtos.add(mediaQueriesDto);
        }

        return new PageImpl<>(mediaQueriesDtos, pageRequest, medias.getTotalElements());
    }

    @Override
    public MediaQueriesDto findOne(String mediaUuid) throws Exception {
        Media media = mediaQueriesService.findOne(mediaUuid);
        MediaQueriesDto mediaQueriesDto = mediaMapper.convertToDto(media);

        if (media.getMediaEntity() != null && !media.getMediaEntity().isEmpty()) {
            mediaQueriesDto.setEntityUuid(media.getMediaEntity().iterator().next().getEntityUuid());
        }

        mediaQueriesDto.add(
                linkTo(methodOn(MediaQueriesController.class).getMedia(mediaUuid)).withSelfRel());
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
    public Resource getFile(String mediaUuid, String res) throws MediaNotFoundException {
        return mediaQueriesService.getFile(mediaUuid, res);
    }
}
