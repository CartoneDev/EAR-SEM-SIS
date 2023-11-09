package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Prerequisite extends AbstractEntity{
    @ManyToOne(optional = false)
    private Course prerequisiteCourse;

    @OneToOne(optional = false)
    private Course requestedCourse;

    @Basic(optional = false)
    private Boolean isMandatory;
}
