package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("GARANT")
@Getter @Setter @NoArgsConstructor
public class Guarantor extends Teacher{
    @OneToMany
    @OrderBy("name ASC")
    List<Course> course;
}
