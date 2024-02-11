package cz.cvut.fel.ear.sis.model;

import cz.cvut.fel.ear.sis.utility.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@NamedQuery(name = "findCourseByName",
        query = "SELECT c FROM Course c WHERE c.name = :name")
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

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public void setGuarantor(Guarantor guarantor) {
        Utils.checkParametersNotNull(guarantor);
        this.guarantor = guarantor;
    }

    public void removeGuarantor(Guarantor guarantor) {
        Utils.checkParametersNotNull(guarantor);
        if (this.guarantor == null) {
            return;
        }
        if (this.guarantor.getId().equals(guarantor.getId())) {
            this.guarantor = null;
        }
    }

    public void addTeacher(Teacher teacher) {
        Utils.checkParametersNotNull(teacher);
        teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        Utils.checkParametersNotNull(teacher);
        if (teachers == null) {
            return;
        }
        teachers.removeIf(t -> t.getId().equals(teacher.getId()));
    }

    public void addPrerequisite(Prerequisite prerequisite) {
        Utils.checkParametersNotNull(prerequisite);
        prerequisites.add(prerequisite);
    }

    public void removePrerequisite(Prerequisite prerequisite) {
        Utils.checkParametersNotNull(prerequisite);
        if (prerequisites == null) {
            return;
        }
        prerequisites.removeIf(p -> p.getId().equals(prerequisite.getId()));
    }

    public void addEnrollmentRecord(EnrollmentRecord enrollmentRecord) {
        Utils.checkParametersNotNull(enrollmentRecord);
        enrollmentRecords.add(enrollmentRecord);
    }
}
