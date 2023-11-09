package cz.cvut.fel.ear.sis.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Program extends AbstractEntity{
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String description;
    @OneToMany(mappedBy = "program", orphanRemoval = true)
    List<Attendance> attendances;
}
