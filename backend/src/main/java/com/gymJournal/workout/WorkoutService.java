package com.gymJournal.workout;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;


    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    List<WorkoutGraphDTO> getGraphData(Long exerciseId,Integer member_id,String param){
        if(Objects.equals(param, "5days")) {
            return workoutRepository.getData5days(exerciseId,member_id)
                    .stream()
                    .map(workout -> new WorkoutGraphDTO(
                            workout.getDate(),
                            workout.getSets()
                                    .stream()
                                    .max(Comparator.comparing(v -> v.getWeight()))
                                    .orElse(null)
                    )).collect(Collectors.toList());
//            return workoutRepository.getData5days(exerciseId,member_id);
        } else if (Objects.equals(param, "5months")) {
            LocalDate currentDate = LocalDate.now();
            // Calculate the start date as three months ago from the current date
            LocalDate startDate = currentDate.minusMonths(5);
            // Set the end date as the current date
            LocalDate endDate = currentDate;
        }
        return null;
    }

    // Get the current date

}
