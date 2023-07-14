package com.gymJournal.workout;

import com.gymJournal.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM workout_set ws WHERE ws.workoutset_id = ?1", nativeQuery = true)
    void deleteWkSet(Long id);
}
