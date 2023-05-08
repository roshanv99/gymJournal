package com.gymJournal.exercise;

public class ExerciseNotFoundException extends RuntimeException {
    ExerciseNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
