package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@Getter @Setter
@NoArgsConstructor
public class Student extends User{
    @Column(name = "student_number")
    private String studentNumber;
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EnrollmentRecord> enrollmentRecords = new ArrayList<>();
    @ManyToOne(optional = true)
    private Program program;
    @ManyToMany
    @JoinTable(
            name = "student_schedule",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id"))
    private List<Schedule> schedules;

    public void addCourse(Course course) {
        EnrollmentRecord enrollmentRecord = new EnrollmentRecord();
        enrollmentRecord.setCourse(course);
        enrollmentRecord.setStudent(this);
        enrollmentRecords.add(enrollmentRecord);
    }
}
