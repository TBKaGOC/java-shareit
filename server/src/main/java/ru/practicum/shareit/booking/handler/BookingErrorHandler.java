package ru.practicum.shareit.booking.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.CorruptedDataException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.exception.InvalidHostException;
import ru.practicum.shareit.booking.exception.NotFoundException;

@RestControllerAdvice("ru.practicum.shareit.booking")
public class BookingErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(NotFoundException e) {
        return new ErrorResponse("NotFoundException", e.getMessage());
    }

    @ExceptionHandler({CorruptedDataException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse corruptedData(Exception e) {
        return new ErrorResponse("CorruptedDataException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse invalidHost(InvalidHostException e) {
        return new ErrorResponse("InvalidHostException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unavailableItem(UnavailableItemException e) {
        return new ErrorResponse("UnavailableItemException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse anyThrowable(Throwable e) {
        return new ErrorResponse("UnknownError", "Возникла неизвестная ошибка: " + e.getMessage());
    }
}
