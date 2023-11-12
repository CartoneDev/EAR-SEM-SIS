package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User{
    @OneToMany(mappedBy = "teacher")
    private List<Schedule> schedules;
}
