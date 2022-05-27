package app.homsai.engine.media.application.http.controllers;

import io.swagger.annotations.*;
import app.homsai.engine.SwaggerApiPageable;
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

@Api(tags = DocsConsts.DOCS_TAGS_MEDIA)
@RestController
public class MediaQueriesController {

    @Autowired
    MediaQueriesApplicationService mediaQueriesApplicationService;

    @ApiOperation(value = "Get the list of media", nickname = "getMedia",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_OK,
            message = "Successfully retrieved list", response = MediaQueriesDto[].class)})
    @SwaggerApiPageable
    @RequestMapping(value = "/media", method = RequestMethod.GET)
    public ResponseEntity getMediaList(@PageableDefault(sort = {"uuid"}) Pageable pageRequest,
            @RequestParam(value = "search", required = false) String search) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findAll(pageRequest, search, null));
    }

    @ApiOperation(value = "Get the list of media of a specific entity", nickname = "getMediaEntity",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @ApiResponses(value = {
            @ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_OK,
                    message = "Successfully retrieved paged list", response = Page.class),
            @ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_CONTENT,
                    message = "The paged content array", response = MediaQueriesDto[].class)})
    @SwaggerApiPageable
    @RequestMapping(value = "/media/entities/{entityUuid}", method = RequestMethod.GET)
    public ResponseEntity getMediaListByEntity(
            @PageableDefault(sort = {"uuid"}) Pageable pageRequest,
            @RequestParam(value = "search", required = false) String search,
            @PathVariable("entityUuid") String entityUuid) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findAll(pageRequest, search, entityUuid));
    }

    @ApiOperation(value = "Get a single media", nickname = "getSingleMedia",
            response = ResponseEntity.class,
            authorizations = {@Authorization(value = DocsConsts.DOCS_HEADER_SECURITY_REF)})
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_OK,
            message = "Successfully retrieved element", response = MediaQueriesDto.class)})
    @RequestMapping(value = "/media/{mediaUuid}", method = RequestMethod.GET)
    public ResponseEntity getMedia(@PathVariable("mediaUuid") String mediaUuid) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaQueriesApplicationService.findOne(mediaUuid));
    }

    @ApiOperation(value = "Get the media to download it", nickname = "downloadMedia",
            response = ResponseEntity.class)
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_OK,
            message = "Successfully downloaded the file", response = InputStreamResource.class)})
    @ApiImplicitParams({@ApiImplicitParam(name = DocsConsts.DOCS_HEADER_AUTHORIZATION)})
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

    @ApiOperation(value = "Get the media to view it", nickname = "viewMedia",
            response = ResponseEntity.class)
    @ApiResponses(value = {@ApiResponse(code = DocsConsts.DOCS_RESPONSE_CODE_OK,
            message = "Successfully viewed element", response = InputStreamResource.class)})
    @ApiImplicitParams({@ApiImplicitParam(name = DocsConsts.DOCS_HEADER_AUTHORIZATION)})
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
