package app.homsai.engine.media.application.http.controllers;

import app.homsai.engine.media.application.services.MediaCommandsApplicationService;
import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Giacomo Agostini on 02/12/16.
 */

@RestController
public class MediaCommandsController {

    @Autowired
    MediaCommandsApplicationService mediaCommandsApplicationService;

    @RequestMapping(value = {"/media"}, method = RequestMethod.POST)
    public ResponseEntity createMedia(@RequestParam("file") MultipartFile file, @RequestParam(
                    value = "uuid", required = false) String uuid, @RequestParam(
                    value = "custom_tag", required = false) String tag)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaCommandsApplicationService.create(file, uuid, tag));
    }



    @RequestMapping(value = "/media/{mediaUuid}", method = RequestMethod.DELETE)
    public ResponseEntity updateObject(@PathVariable("mediaUuid") String mediaUuid)
            throws MediaNotFoundException {
        mediaCommandsApplicationService.delete(mediaUuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = {"/media/{mediaUuid}/entity/{entityUuid}"}, method = RequestMethod.POST)
    public ResponseEntity associateMediaEntity(@PathVariable("mediaUuid") String mediaUuid,
            @PathVariable("entityUuid") String entityUuid) throws MediaNotFoundException {
        mediaCommandsApplicationService.associateMediaEntity(mediaUuid, entityUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/media/{mediaUuid}/entity/{entityUuid}", method = RequestMethod.DELETE)
    public ResponseEntity disassociateMediaEntity(@PathVariable("mediaUuid") String mediaUuid,
            @PathVariable("entityUuid") String entityUuid) throws MediaNotFoundException {
        mediaCommandsApplicationService.disassociateMediaEntity(mediaUuid, entityUuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
