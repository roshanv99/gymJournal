package com.gymJournal.workout;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gymJournal.exercise.Exercise;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gymJournal.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Workout {

    @Id
    @SequenceGenerator(
            name = "workout_seq",
            sequenceName = "workout_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "workout_seq")
    @JsonProperty("workout_id")
    private Long workout_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    //CASCADE HELPS US INSERT DEPENDENT ROWS AUTOMATICALLY
    //Mapped by 'workout' inside WorkoutSet
    @JsonIgnoreProperties("workout")
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutSet> sets;

    private Integer strength;

    public Workout() {}

    public Workout(Exercise exercise, Member member, List<WorkoutSet> sets, Integer strength, LocalDate date) {
        this.exercise = exercise;
        this.member = member;
        this.sets = sets;
        this.strength = strength;
        this.date = date;
    }

    public Long getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(Long workout_id) {
        this.workout_id = workout_id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public List<WorkoutSet> getSets() {
        return sets;
    }

    public void setSets(List<WorkoutSet> sets) {
        this.sets = sets;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
