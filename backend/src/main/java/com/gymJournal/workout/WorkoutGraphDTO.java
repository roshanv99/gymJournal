package com.gymJournal.workout;

import java.time.LocalDate;

public record WorkoutGraphDTO (
        LocalDate date,
        WorkoutSet workoutSet
){
}
