package app.homsai.engine.media.application.http.controllers;

import app.homsai.engine.media.domain.exceptions.MediaNotFoundException;
import app.homsai.engine.media.domain.exceptions.MediaNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;


/**
 * Created by Giacomo Agostini on 02/12/16.
 */

@ControllerAdvice
public class MediaExceptionsController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "media not found")
    @ExceptionHandler(MediaNotFoundException.class)
    public void handleMediaNotFoundException(MediaNotFoundException mediaNotFoundException) {
        // mediaNotFoundException.printStackTrace();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "media not supported")
    @ExceptionHandler(MediaNotSupportedException.class)
    public void handleMediaNotSupportedException(
            MediaNotSupportedException mediaNotSupportedException) {
        // mediaNotSupportedException.printStackTrace();
    }



    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "SQL error")
    @ExceptionHandler(SQLException.class)
    public void handleSQLException(SQLException sqlException) {
        // sqlException.printStackTrace();
    }


}
