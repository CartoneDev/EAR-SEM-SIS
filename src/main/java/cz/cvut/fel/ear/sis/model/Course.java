package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
public class Course extends AbstractEntity{
    @Setter
    @Basic(optional = false)
    private String name;
    @Setter
    @Basic(optional = false)
    private String description;
    @Basic(optional = false)
    private Integer credits;
    @Basic(optional = false)
    private Integer hoursLecture;
    @Basic(optional = false)
    private Integer hoursPractise;

    @OneToMany(mappedBy = "prerequisiteCourse")
    @Setter
    private List<Prerequisite> prerequisites = new ArrayList<>();

    @OneToOne
    private Guarantor guarantor;

    @ManyToMany
    @JoinTable(
            name = "course_teacher",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers= new ArrayList<>();

    @OneToMany(mappedBy = "course")
    List<EnrollmentRecord> enrollmentRecords= new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    List<Schedule> schedules= new ArrayList<>();

    public void setCredits(Integer credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Credits cannot be negative");
        }
        this.credits = credits;
    }
    public void setHoursLecture(Integer hoursLecture) {
        if (hoursLecture < 0) {
            throw new IllegalArgumentException("Hours of lecture cannot be negative");
        }
        this.hoursLecture = hoursLecture;
    }
    public void setHoursPractise(Integer hoursPractise) {
        if (hoursPractise < 0) {
            throw new IllegalArgumentException("Hours of practise cannot be negative");
        }
        this.hoursPractise = hoursPractise;
    }
}
