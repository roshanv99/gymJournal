package com.gymJournal.exercise;

import com.gymJournal.category.Category;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Exercise {

    @Id
    @SequenceGenerator(
            name = "exercise_seq",
            sequenceName = "exercise_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_seq"
    )
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String image;

    private String tutorial;

    public Exercise() {}

    public Exercise(String title, Category category, String image, String tutorial) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.tutorial = tutorial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise exercise)) return false;
        return Objects.equals(id, exercise.id) && Objects.equals(title, exercise.title) && Objects.equals(category, exercise.category) && Objects.equals(image, exercise.image) && Objects.equals(tutorial, exercise.tutorial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, image, tutorial);
    }
}

