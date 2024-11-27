package ru.practicum.shareit.request.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.request.exception.NotFoundException;

@RestControllerAdvice("ru.practicum.shareit.request")
public class RequestErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(NotFoundException e) {
        return new ErrorResponse("NotFoundException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse corruptedData(MethodArgumentNotValidException e) {
        return new ErrorResponse("CorruptedDataException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse anyThrowable(Throwable e) {
        return new ErrorResponse("UnknownError", "Возникла неизвестная ошибка: " + e.getMessage());
    }
}