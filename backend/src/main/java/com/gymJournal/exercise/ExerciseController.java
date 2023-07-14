package com.gymJournal.exercise;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    CollectionModel<EntityModel<Exercise>> all() {

        List<EntityModel<Exercise>> exercises = repository.findAll().stream()
                .map(exercise -> EntityModel.of(exercise,
                        linkTo(methodOn(ExerciseController.class).one(exercise.getExercise_id())).withSelfRel(),
                        linkTo(methodOn(ExerciseController.class).all()).withRel("exercises")))
                .collect(Collectors.toList());

        return CollectionModel.of(exercises, linkTo(methodOn(ExerciseController.class).all()).withSelfRel());
    }
    @GetMapping("/fetch={id}")
    List<Exercise> findAllByCategoryId(@PathVariable Long id){
        return repository.findByCategoryId(id);
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
                    newExercise.setExercise_id(id);
                    return repository.save(newExercise);
                });
    }

    @DeleteMapping("{id}")
    void deleteExercise(@PathVariable Long id) {
        repository.deleteById(id);
    }
}