package ru.practicum.shareit.user.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;

@RestControllerAdvice("ru.practicum.shareit.user")
public class UserErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, CorruptedDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse corruptedData(Exception e) {
        return new ErrorResponse("CorruptedDataException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse duplicatedData(DuplicateDataException e) {
        return new ErrorResponse("DuplicatedDataException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse anyThrowable(Throwable e) {
        return new ErrorResponse("UnknownError", "Возникла неизвестная ошибка: " + e.getMessage());
    }
}
