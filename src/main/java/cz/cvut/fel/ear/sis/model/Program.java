package cz.cvut.fel.ear.sis.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Program extends AbstractEntity{
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String description;
    @OneToMany(mappedBy = "program", orphanRemoval = true)
    List<Attendance> attendances = new ArrayList<>();
    @OneToMany(mappedBy = "program")
    List<Student> students;
}
