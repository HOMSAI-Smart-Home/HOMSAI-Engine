package app.homsai.engine.common.application.http.controller;

import app.homsai.engine.common.domain.exceptions.*;
import app.homsai.engine.common.domain.models.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BaseExceptionsController {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    ErrorInfo handleBadRequestException(BadRequestException badRequestException) {
        return new ErrorInfo(badRequestException.getCode(), badRequestException.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    ErrorInfo handleConflictException(ConflictException conflictException) {
        return new ErrorInfo(conflictException.getCode(), conflictException.getMessage());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    ErrorInfo handleNotFoundException(NotFoundException notFoundException) {
        return new ErrorInfo(notFoundException.getCode(), notFoundException.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    ErrorInfo handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        return new ErrorInfo(unauthorizedException.getCode(), unauthorizedException.getMessage());
    }
}
