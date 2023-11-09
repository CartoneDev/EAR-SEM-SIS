package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @Getter @Setter
public class EnrollmentRecord extends AbstractEntity{
    @OneToOne(optional = false)
    private Student student;
    @OneToOne(optional = false)
    private Course course;
    @Basic(optional = false) // TODO: consider
    private Integer grade;
    @Basic(optional = false)
    private String semYear;
}
