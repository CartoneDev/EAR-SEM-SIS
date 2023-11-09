package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @NoArgsConstructor
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
    List<Prerequisite> prerequisites;
    @OneToOne
    private Guarantor guarantor;

    @OneToMany(mappedBy = "course")
    List<Teacher> teachers;

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
