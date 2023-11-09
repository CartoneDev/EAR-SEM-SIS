package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User{
    @Column(name = "student_number")
    private String studentNumber;
}
