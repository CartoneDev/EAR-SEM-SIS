package cz.cvut.fel.ear.sis.controller;

import cz.cvut.fel.ear.sis.model.Student;
import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Student> getStudentById(int id){
        Student student = studentService.getStudentById(id);
        if (student == null){
            LOG.error("Student {} was not found.", id);
            return ResponseEntity.notFound().build();
        }
        LOG.info("Student {} was found.", student.getFirstName());
        return ResponseEntity.ok(student);
    }
}
