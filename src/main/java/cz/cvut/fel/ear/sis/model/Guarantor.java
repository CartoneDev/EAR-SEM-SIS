package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("GARANT")
@Getter @Setter @NoArgsConstructor
@NamedQuery(name = "findCourseGuarantors", query = "SELECT g FROM Guarantor g JOIN g.course c WHERE c = :course")
public class Guarantor extends Teacher{
    @OneToMany
    @OrderBy("name ASC")
    List<Course> course;
}
