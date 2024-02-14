package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enrollment_record")
@NoArgsConstructor @Getter @Setter
public class EnrollmentRecord extends AbstractEntity{
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;
    @Enumerated(EnumType.STRING)
    private GradeType grade;
    @Basic(optional = false)
    private String semYear;
}
