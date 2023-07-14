package com.gymJournal.workout;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.gymJournal.exercise.ExerciseRepository;
import com.gymJournal.member.Member;
import com.gymJournal.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/workouts")
public class WorkoutController {
    Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;
    private final ExerciseRepository exeRep;
    private final WorkoutSetRepository workoutsetRepository;
    private final MemberRepository memberRep;
    WorkoutController(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, WorkoutService workoutService, ExerciseRepository exeRep, WorkoutSetRepository workoutsetRepository, MemberRepository member) {
        this.workoutRepository = workoutRepository;
        this.workoutService = workoutService;
        this.exeRep = exeRep;
        this.workoutsetRepository = workoutsetRepository;
        this.memberRep = member;
    }

    @PostMapping("/{id}")
    public List<Workout> getWorkoutByExerciseId(@PathVariable Long id, @RequestBody Member member)  {
        logger.info("Member : " + member);
        return workoutRepository.findByExerciseExerciseIdAndMember((long)id, member);
    }

    @GetMapping
    List<Workout> getAllWorkout(){
        return workoutRepository.findAll();
    }

    @PostMapping("/addWorkout")
    public ResponseEntity<Workout> addWorkout(@RequestBody Workout resWorkout) {
//        Optional<Exercise> exerciseOptional = exeRep.findById(Long.valueOf(31));
//        Exercise exercise = exerciseOptional.orElseThrow(() -> new EntityNotFoundException("Exercise not found with ID: 31"));
//
//        Optional<Member> memeberOptional = memberRep.findMemberByEmail("gaylene.robel@gymJournal.com");
//        Member mem = memeberOptional.orElseThrow(() -> new EntityNotFoundException("Exercise not found with ID: 31"));

        try {
            Workout workout = new Workout();
            workout.setMember(resWorkout.getMember());
            workout.setExercise(resWorkout.getExercise());
            workout.setDate(resWorkout.getDate());
            workout.setStrength(resWorkout.getStrength());

            List<WorkoutSet> workoutSets = resWorkout.getSets();
            if (workoutSets != null) {
                for (WorkoutSet workoutSet : workoutSets) {
                    workoutSet.setWorkout(workout);
                }
            }

            workout.setSets(resWorkout.getSets());
            Workout savedWorkout = workoutRepository.save(workout);
            logger.trace("Workout Saved: ", savedWorkout);
            return ResponseEntity.ok(savedWorkout);
        } catch (Exception e){
            logger.error(String.valueOf("Failed to save Workout: " + e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/getPR")
    WorkoutSet getPR(@RequestParam("exercise") Long exercise_id, @RequestParam("member_id") Integer member_id){
        // Add codd to make sure only the authenticated user has access to this data
        return workoutRepository.getPR(exercise_id,member_id);
    }

    @GetMapping("/getGraphData")
    List<WorkoutGraphDTO> getGraphData(@RequestParam("exercise") Long exercise_id, @RequestParam("member_id") Integer member_id,@RequestParam("param") String param){
        return workoutService.getGraphData(exercise_id, member_id,param);
    }

    @GetMapping("/getWorkoutByDate")
    ResponseEntity<List<Workout>> getWorkoutByDate(@DateTimeFormat() LocalDate date, @RequestParam("member_id") Integer member_id){
        // Add codd to make sure only the authenticated user has access to this data
        try {
            logger.info("Success!");
            return ResponseEntity.ok(workoutRepository.getWorkoutByDateAndMemberId(date,member_id));
        } catch(Exception e){
            logger.error("Failed to fetch Data by Date: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getDates")
    ResponseEntity<List<LocalDate>> getWorkoutDates(@RequestParam("member_id") Integer member_id){
        try {
            logger.info("Success!");
            return ResponseEntity.ok(workoutRepository.getDateByMemberId(member_id));
        } catch(Exception e){
            logger.error("Failed to fetch Data by Date: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable Long id, @RequestBody Workout workout) {
        Optional<Workout> existingWorkout = workoutRepository.findById(id);
        try{
            if(existingWorkout.isPresent()){
                Workout updatedWorkout = existingWorkout.get();

                //Get Ids of Sets we found from DB
                List<Long> oldSets = new ArrayList<>();
                for(final WorkoutSet set : updatedWorkout.getSets()){
                    oldSets.add(set.getWorkoutset_id());
                }

                //Get Ids of Sets we found from got from Payload
                List<Long> newSets = new ArrayList<>();
                for(final WorkoutSet set : workout.getSets()){
                    newSets.add(set.getWorkoutset_id());
                }

                logger.info("Remove Old Sets: " +  oldSets);
                List<WorkoutSet> workoutSets = workout.getSets();
                logger.info("New Sets: " +  newSets);

                // Get all ids which are not common in both
                Set<Long> union = new HashSet<Long>(oldSets);
                union.addAll(newSets);
                // Prepare an intersection
                Set<Long> intersection = new HashSet<Long>(oldSets);
                intersection.retainAll(newSets);
                // Subtract the intersection from the union
                union.removeAll(intersection);
                if(union != null) {
                    for (Long n : union) {
                        //Delete Ids that are to be deleted
                        logger.info("Deleting: " +  n);
                        workoutsetRepository.deleteWkSet(n);
                    }
                }


                updatedWorkout.getSets().clear();

                if (workoutSets != null) {
                    for (WorkoutSet workoutSet : workoutSets) {
                        workoutSet.setWorkout(updatedWorkout);
                    }
                }

                updatedWorkout.setSets(workoutSets);
                Workout savedWorkout = workoutRepository.save(updatedWorkout);
                logger.info("Updating Workout: ", savedWorkout);
                return ResponseEntity.ok(savedWorkout);
            } else {
                logger.info("Workout not found",workout);
                return ResponseEntity.notFound().build();
            }
        } catch(Exception e) {
            logger.error("Failed to Save workout: ",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//        return ResponseEntity.ok(workout);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        try {
            Optional<Workout> existingWorkout = workoutRepository.findById(id);
            if (existingWorkout.isEmpty()) {
                logger.info("Workout not found");
                return ResponseEntity.notFound().build();
            }
            logger.info("Deleted Workout");
            workoutRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            logger.info("Unable to Delete Workout: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


