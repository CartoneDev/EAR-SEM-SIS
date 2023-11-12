package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static cz.cvut.fel.ear.sis.model.GradeType.UNGRADED;

@Entity
@NoArgsConstructor @Getter @Setter
public class EnrollmentRecord extends AbstractEntity{
    @OneToOne(optional = false)
    private Student student;
    @OneToOne(optional = false)
    private Course course;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false) // TODO: consider
    private GradeType grade = UNGRADED;
    @Basic(optional = false)
    private String semYear;
}
