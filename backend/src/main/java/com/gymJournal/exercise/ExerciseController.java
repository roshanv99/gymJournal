package com.gymJournal.exercise;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/exercises")
class ExerciseController {

    private final ExerciseRepository repository;

    ExerciseController(ExerciseRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping
    List<Exercise> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping
    Exercise newExercise(@RequestBody Exercise newExercise) {
        return repository.save(newExercise);
    }

    // Single item

    @GetMapping("{id}")
    Exercise one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @PutMapping("{id}")
    Exercise replaceExercise(@RequestBody Exercise newExercise, @PathVariable Long id) {

        return repository.findById(id)
                .map(exercise -> {
                    exercise.setTitle(newExercise.getTitle());
                    return repository.save(exercise);
                })
                .orElseGet(() -> {
                    newExercise.setId(id);
                    return repository.save(newExercise);
                });
    }

    @DeleteMapping("{id}")
    void deleteExercise(@PathVariable Long id) {
        repository.deleteById(id);
    }
}