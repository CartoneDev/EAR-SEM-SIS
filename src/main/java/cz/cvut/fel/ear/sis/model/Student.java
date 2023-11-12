package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@Getter @Setter
@NoArgsConstructor
public class Student extends User{
    @Column(name = "student_number")
    private String studentNumber;
    @OneToMany(mappedBy = "student")
    private List<EnrollmentRecord> enrollmentRecords = new ArrayList<>();
    @ManyToOne(optional = false)
    private Program program;
    @ManyToMany
    @JoinTable(
            name = "student_schedule",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id"))
    private List<Schedule> schedules;
}
