package cz.cvut.fel.ear.sis.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.EnrollmentRecord;
import cz.cvut.fel.ear.sis.model.GradeType;
import cz.cvut.fel.ear.sis.model.Student;
import jakarta.persistence.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnrollmentDTO {
    private UserDTO student;
    private CourseDTO course;
    private GradeType grade;
    private String semYear;
    public EnrollmentDTO(EnrollmentRecord enrollmentRecord){
        this.student = new UserDTO(enrollmentRecord.getStudent());
        this.course = new CourseDTO(enrollmentRecord.getCourse());
        this.grade = enrollmentRecord.getGrade();
        this.semYear = enrollmentRecord.getSemYear();
    }
}
