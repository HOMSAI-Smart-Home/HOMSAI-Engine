package app.homsai.engine.media.application.http.controllers;

import io.swagger.annotations.*;
import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.media.application.http.dtos.MediaQueriesDto;
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

@Api(tags = DocsConsts.DOCS_TAGS_MEDIA)
@RestController
public class MediaCommandsController {

    @Autowired
    MediaCommandsApplicationService mediaCommandsApplicationService;

    @ApiOperation(value = "Create a media", nickname = "addMedia", response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_CREATED,
            message = "Successfully created  media", response = MediaQueriesDto.class)})
    @RequestMapping(value = {"/media"}, method = RequestMethod.POST)
    public ResponseEntity createMedia(@RequestParam("file") MultipartFile file,
            @ApiParam(name = "uuid", value = "The entity uuid to associate") @RequestParam(
                    value = "uuid", required = false) String uuid,
            @ApiParam(name = "custom_tag", value = "A tag to give to the media") @RequestParam(
                    value = "custom_tag", required = false) String tag)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaCommandsApplicationService.create(file, uuid, tag));
    }

    @ApiOperation(value = "Create a QR code media", nickname = "addQRCodeMedia",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_CREATED,
            message = "Successfully created  media", response = MediaQueriesDto.class)})
    @RequestMapping(value = {"/media/qrcode"}, method = RequestMethod.POST)
    public ResponseEntity createQRCode(
            @ApiParam(name = "uuid", value = "The entity uuid to associate") @RequestParam(
                    value = "uuid", required = false) String uuid,
            @ApiParam(name = "custom_tag", value = "A tag to give to the media") @RequestParam(
                    value = "custom_tag", required = false) String tag)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaCommandsApplicationService.createQRCode(uuid, tag));
    }

    @ApiOperation(value = "Delete a media", nickname = "deeleteMedia",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @RequestMapping(value = "/media/{mediaUuid}", method = RequestMethod.DELETE)
    public ResponseEntity updateObject(@PathVariable("mediaUuid") String mediaUuid)
            throws MediaNotFoundException {
        mediaCommandsApplicationService.delete(mediaUuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Associate a media to an entity", nickname = "associateMediaToEntity",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @RequestMapping(value = {"/media/{mediaUuid}/entity/{entityUuid}"}, method = RequestMethod.POST)
    public ResponseEntity associateMediaEntity(@PathVariable("mediaUuid") String mediaUuid,
            @PathVariable("entityUuid") String entityUuid) throws MediaNotFoundException {
        mediaCommandsApplicationService.associateMediaEntity(mediaUuid, entityUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Deassociate a media from an entity",
            nickname = "deassociateMediaToEntity", response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @RequestMapping(value = "/media/{mediaUuid}/entity/{entityUuid}", method = RequestMethod.DELETE)
    public ResponseEntity disassociateMediaEntity(@PathVariable("mediaUuid") String mediaUuid,
            @PathVariable("entityUuid") String entityUuid) throws MediaNotFoundException {
        mediaCommandsApplicationService.disassociateMediaEntity(mediaUuid, entityUuid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
