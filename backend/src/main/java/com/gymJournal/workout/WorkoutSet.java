package com.gymJournal.workout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","workout"})
@Table(name = "workout_set")
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutset_id;

    private Integer set_num;
    private Double weight;
    private Integer reps;
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    public WorkoutSet(Long workoutset_id, Integer set_num, Double weight, Integer reps, Workout workout) {
        this.workoutset_id = workoutset_id;
        this.set_num = set_num;
        this.weight = weight;
        this.reps = reps;
        this.workout = workout;
    }

    public WorkoutSet(){}

    public Long getWorkoutset_id() {
        return workoutset_id;
    }

    public void setWorkoutset_id(Long workoutset_id) {
        this.workoutset_id = workoutset_id;
    }

    public Integer getSet_num() {
        return set_num;
    }

    public void setSet_num(Integer set_num) {
        this.set_num = set_num;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
