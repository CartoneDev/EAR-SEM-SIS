package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User{
    @OneToMany(mappedBy = "teacher")
    private List<Schedule> schedules;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;
}
