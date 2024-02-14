package cz.cvut.fel.ear.sis.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import cz.cvut.fel.ear.sis.model.*;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CourseDTO {
    public Integer id;
    public String name;
    public String description;
    public Integer credits;
    public Integer hoursLecture;
    public Integer hoursPractise;
    public UserDTO guarantor;
    public ArrayList<UserDTO> teachers;
    public CourseDTO(Course course){
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.credits = course.getCredits();
        this.hoursLecture = course.getHoursLecture();
        this.hoursPractise = course.getHoursPractise();
        this.guarantor = new UserDTO(course.getGuarantor());
        this.teachers = new ArrayList<UserDTO>( course.getTeachers().stream().map(UserDTO::new).toList());
        this.teachers.add(0, this.guarantor);
    }
}
