package br.com.picpay.picpaysimplificado.infra.exception;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDuplicateEntry(DataIntegrityViolationException e ){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getLocalizedMessage());
        problemDetail.setTitle("User already exists");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail handleEntityNotFound(EntityNotFoundException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(PicPayGeneralException.class)
    ProblemDetail handleInternalServerError(PicPayGeneralException e ){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        problemDetail.setTitle(e.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
