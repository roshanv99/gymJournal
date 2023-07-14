package com.gymJournal.exercise;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
//    @Query("FROM Exercise e where e.category_id = :catId")
    public List<Exercise> findByCategoryId(Long catId);
    //    List<EntityModel<Exercise>> findAllByCategoryId(@Param("catId") Long catId);

}
