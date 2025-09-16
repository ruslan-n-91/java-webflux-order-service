package com.example.javawebfluxorderservice.exceptionhandler;
//
//import com.example.javawebfluxorderservice.exception.OrderNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import reactor.core.publisher.Mono;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(OrderNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Mono<ErrorResponseDto> handleOrderNotFoundException(OrderNotFoundException ex) {
//        return Mono.just(new ErrorResponseDto(
//                HttpStatus.NOT_FOUND.value(),
//                ex.getMessage()
//        ));
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Mono<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
//        return Mono.just(new ErrorResponseDto(
//                HttpStatus.BAD_REQUEST.value(),
//                ex.getMessage()
//        ));
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Mono<ErrorResponseDto> handleException(Exception ex) {
//        return Mono.just(new ErrorResponseDto(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                ex.getMessage()
//        ));
//    }
//}
