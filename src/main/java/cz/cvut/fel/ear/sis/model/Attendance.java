package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Attendance extends AbstractEntity{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType attendanceType;
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @ManyToOne(optional = false)
    private Program program;
}
