package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter @Setter
public class Schedule extends AbstractEntity{
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime from;
    @Column(name = "date_to", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime to;

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_type", nullable = false)
    private LectureType lectureType;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "schedule")
    private List<Student> students;

    @ManyToOne(optional = false)
    private Course course;

    @ManyToOne(optional = false)
    private Teacher teacher;

    @ManyToOne(optional = false)
    private Room room;
    @Override
    public String toString() {
        return "Schedule{" +
                "from=" + from +
                ", to=" + to +
                ", room=" + room +
                ", capacity=" + capacity +
                '}';
    }
}
