package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@Getter @Setter
@NoArgsConstructor
public class Student extends User{
    @Column(name = "student_number")
    private String studentNumber;
    @ManyToMany(mappedBy = "students")
    private List<EnrollmentRecord> enrollmentRecords;
    @ManyToOne(optional = false)
    private Program program;
    @OneToMany(mappedBy = "student")
    private List<Schedule> schedules;
}
