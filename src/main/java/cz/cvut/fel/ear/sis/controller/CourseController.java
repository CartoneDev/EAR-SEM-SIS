package cz.cvut.fel.ear.sis.controller;

import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Student;

import java.util.List;

@RestController
@RequestMapping("/course")
//@PreAuthorize("hasRole('USER')")
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final StudentService studentService;

    @Autowired
    public CourseController(CourseService courseService, StudentService studentService){
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> addCourseToPlan(@RequestBody Course course, @RequestBody Student student) {        studentService.addCourseToPlan(course, student);
        LOG.info("Course {} was added to plan of student {}.", course.getName(), student.getFirstName());
        return ResponseEntity.ok(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> removeCourseFromPlan(@RequestBody Course course, @RequestBody Student student) {        studentService.removeCourseFromPlan(course, student);
        LOG.info("Course {} was removed from plan of student {}.", course.getName(), student.getFirstName());
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/register/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseById(@PathVariable int id){
        Course course = courseService.findCourseById(id);
        if (course == null){
            LOG.error("Course {} was not found.", id);
            return ResponseEntity.notFound().build();
        }
        LOG.info("Course {} was found.", course.getName());
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.findAllCourses();
        if (courses == null){
            LOG.error("Courses were not found.");
            return ResponseEntity.notFound().build();
        }
        LOG.info("All courses were found.");
        return ResponseEntity.ok((courses));
    }

    @GetMapping(value = "/course/search/?name={name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> searchCourseByName(@PathVariable String name){
        Course courses = courseService.findCourseByName(name);
        if (courses == null){
            LOG.error("Course {} was not found.", name);
            return ResponseEntity.notFound().build();
        }
        LOG.info("Course {} was found.", name);
        return ResponseEntity.ok(courses);
    }

    @GetMapping(value = "/student/{id}/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable int student_id){
        Student student = studentService.findStudentById(student_id);
        
        List<Course> students = courseService.listStudentCourses(student);
        if (students == null){
            LOG.error("Courses of student {} were not found.", student.getFirstName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Students {} were found.", students);
        return ResponseEntity.ok(students);
    }

    public ResponseEntity<List<Course>> getListStudentCurrentCourses(Student student){
        List<Course> courses = courseService.listStudentCurrentCourses(student);
        if (courses == null){
            LOG.error("Courses of student {} were not found.", student.getFirstName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Current courses: {courses}");
        return ResponseEntity.ok(courses);
    }
}
