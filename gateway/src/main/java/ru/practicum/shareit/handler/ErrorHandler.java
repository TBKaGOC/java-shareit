package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.CorruptedDataException;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, CorruptedDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse corruptedData(Exception e) {
        return new ErrorResponse("corruptedData", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse anyThrowable(Throwable e) {
        return new ErrorResponse("UnknownError", "Возникла неизвестная ошибка: " + e.getMessage());
    }
}
