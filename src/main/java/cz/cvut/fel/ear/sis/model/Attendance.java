package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;

@Entity
public class Attendance extends AbstractEntity{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType attendanceType;
    @OneToOne(optional = false)
    private Course course;
    @ManyToOne(optional = false)
    private Program program;
}
