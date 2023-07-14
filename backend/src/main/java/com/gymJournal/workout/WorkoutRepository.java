package com.gymJournal.workout;

import com.gymJournal.exercise.Exercise;
import com.gymJournal.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
//    List<Workout> findByExerciseExerciseId(Long exerciseId);
    List<Workout> findByExerciseExerciseIdAndMember(Long exerciseId, Member member);

    @Query("SELECT ws FROM WorkoutSet ws " +
            "JOIN ws.workout w " +
            "WHERE w.exercise.exerciseId = :exerciseId " +
            "AND w.member.id = :member_id " +
            "ORDER BY ws.weight DESC,ws.weight DESC LIMIT 1")
    WorkoutSet getPR(Long exerciseId,Integer member_id);

    @Query("SELECT DISTINCT w FROM Workout w " +
            "WHERE w.exercise.exerciseId = :exerciseId " +
            "AND w.member.id = :member_id " +
            "ORDER BY w.date DESC LIMIT 5")
    List<Workout> getData5days(Long exerciseId,Integer member_id);

    Optional<Workout> findById(Long id);

    List<Workout> getWorkoutByDateAndMemberId(LocalDate date,Integer member_id);

    @Query("SELECT DISTINCT w.date FROM Workout w")
    List<LocalDate> getDateByMemberId(Integer member_id);
}
