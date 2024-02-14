package cz.cvut.fel.ear.sis.controller;

import cz.cvut.fel.ear.sis.DTO.CourseDTO;
import cz.cvut.fel.ear.sis.DTO.UserDTO;
import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Student;
import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping(value = "/student/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getStudentById(@PathVariable int id){
        Student student = studentService.getStudentById(id);
        if (student == null){
            LOG.error("Student {} was not found.", id);
            return ResponseEntity.notFound().build();
        }
        LOG.info("Student {} was found.", student.getFirstName());
        return ResponseEntity.ok(new UserDTO(student));
    }

    @GetMapping(value = "/courses/my/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCoursesOfStudent(@PathVariable int id){
        Student student = studentService.getStudentById(id);
        if (student == null){
            LOG.error("Student {} was not found.", id);
            return ResponseEntity.notFound().build();
        }
        List<CourseDTO> courses = studentService.getAllCoursesOfStudent(student);
        LOG.info("Courses of student {} were found.", student.getFirstName());
        return ResponseEntity.ok(courses);
    }



}
