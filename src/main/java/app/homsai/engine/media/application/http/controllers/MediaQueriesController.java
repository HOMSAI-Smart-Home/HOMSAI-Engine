package app.homsai.engine.media.application.http.controllers;

import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
import app.homsai.engine.media.application.services.MediaQueriesApplicationService;
import app.homsai.engine.media.domain.services.MultipartFileSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Giacomo Agostini on 02/12/16.
 */

@RestController
public class MediaQueriesController {

    @Autowired
    MediaQueriesApplicationService mediaQueriesApplicationService;

    @RequestMapping(value = "/media", method = RequestMethod.GET)
    public ResponseEntity getMediaList(@PageableDefault(sort = {"uuid"}) Pageable pageRequest,
            @RequestParam(value = "search", required = false) String search) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findAll(pageRequest, search, null));
    }


    @RequestMapping(value = "/media/entities/{entityUuid}", method = RequestMethod.GET)
    public ResponseEntity getMediaListByEntity(
            @PageableDefault(sort = {"uuid"}) Pageable pageRequest,
            @RequestParam(value = "search", required = false) String search,
            @PathVariable("entityUuid") String entityUuid) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findAll(pageRequest, search, entityUuid));
    }


    public ResponseEntity getMedia(@PathVariable("mediaUuid") String mediaUuid) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findOne(mediaUuid));
    }

    @RequestMapping(value = "/media/{mediaUuid}/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable("mediaUuid") String mediaUuid,
            @RequestParam(value = "res", required = false) String res) throws Exception {
        String extension = mediaQueriesApplicationService.findOne(mediaUuid).getOriginalExtension();
        Resource file = mediaQueriesApplicationService.getFile(mediaUuid, res);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "." + extension + "\"")
                .body(new InputStreamResource(file.getInputStream()));
    }


    @RequestMapping(value = "/media/{mediaUuid}/view", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewMedia(
            @PathVariable("mediaUuid") String mediaUuid,
            @RequestParam(value = "res", required = false) String res) throws Exception {
        MediaQueriesDto mediaQueriesDto = mediaQueriesApplicationService.findOne(mediaUuid);
        Resource file = mediaQueriesApplicationService.getFile(mediaUuid, res);
        String mimeType = mediaQueriesDto.getMimetype();
        if (res != null && res.equalsIgnoreCase("THUMB")) {
            mimeType = "image/png";
        }
        return ResponseEntity.ok().contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(file.getInputStream()));
    }

    @RequestMapping(value = "/media/video/{mediaUuid}", method = RequestMethod.GET)
    public ResponseEntity streamVideo(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("mediaUuid") String mediaUuid) throws Exception {
        mediaUuid = mediaUuid.split(".mp4")[0];
        MultipartFileSender
                .fromFile(mediaQueriesApplicationService.getFile(mediaUuid, null).getFile())
                .with(request).with(response).serveResource();
        return null;
    }

}
