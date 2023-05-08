package com.gymJournal.exercise;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
class ExerciseNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ExerciseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(ExerciseNotFoundException ex) {
        return ex.getMessage();
    }
}
