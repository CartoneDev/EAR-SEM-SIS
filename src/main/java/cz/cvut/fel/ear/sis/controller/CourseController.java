package cz.cvut.fel.ear.sis.controller;

import cz.cvut.fel.ear.sis.DTO.CourseDTO;
import cz.cvut.fel.ear.sis.DTO.EnrollmentDTO;
import cz.cvut.fel.ear.sis.DTO.ScheduleDTO;
import cz.cvut.fel.ear.sis.model.*;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
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
    public ResponseEntity<Course> addCourseToPlan(@RequestBody Integer course_id, @RequestBody Integer student_id) {
        try {
            Course course = courseService.findCourseById(course_id);
            Student student = studentService.findStudentById(student_id);
            if (course == null || student == null){
                LOG.error("Course {} or student {} was not found.", course_id, student_id);
                return ResponseEntity.notFound().build();
            }
            studentService.addCourseToPlan(course, student);
            LOG.info("Course {} was added to plan of student {}.", course.getName(), student.getFirstName());
            return ResponseEntity.ok(course);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> removeCourseFromPlan(@RequestBody Integer course_id, @RequestBody Integer student_id) {
        try{
            Course course = courseService.findCourseById(course_id);
            Student student = studentService.findStudentById(student_id);
            if (course == null || student == null){
                LOG.error("Course {} or student {} was not found.", course_id, student_id);
                return ResponseEntity.notFound().build();
            }
            studentService.removeCourseFromPlan(course, student);
            LOG.info("Course {} was removed from plan of student {}.", course.getName(), student.getFirstName());
            return ResponseEntity.ok(course);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<List<CourseDTO>> getAllCourses(){
        List<Course> courses = courseService.findAllCourses();
        if (courses == null){
            LOG.error("Courses were not found.");
            return ResponseEntity.notFound().build();
        }
        LOG.info("All courses were found.");
        return ResponseEntity.ok((courses.stream().map(CourseDTO::new).toList()));
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

    @GetMapping(value = "/student/{student_id}/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getCoursesByStudentId(@PathVariable int student_id){
        Student student = studentService.findStudentById(student_id);
        
        List<Course> courses = courseService.listStudentCurrentCourses(student);
        if (courses == null){
            LOG.error("Courses of student {} were not found.", student.getFirstName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Courses of student - {}:\n \t{}.", courses);
        return ResponseEntity.ok(courses.stream().map(CourseDTO::new).toList());
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

    @GetMapping(value = "/{course_id}/schedule", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<ScheduleDTO>> getCourseSchedule(@PathVariable Integer course_id){
        Course course = courseService.findCourseById(course_id);
        List<Schedule> schedule = courseService.getCourseSchedule(course);
        if (schedule == null){
            LOG.error("Schedule of course {} were not found.", course.getName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Schedule of course {} were found.", course.getName());
        return ResponseEntity.ok(schedule.stream().map(ScheduleDTO::new).toList());
    }

    @GetMapping(value = "/{course_id}/teachers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getCourseTeachers(@PathVariable Integer course_id){
        Course course = courseService.findCourseById(course_id);
        List<Teacher> teachers = courseService.getCourseTeachers(course);
        if (teachers == null){
            LOG.error("Teachers of course {} were not found.", course.getName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Teachers of course {} were found.", course.getName());
        return ResponseEntity.ok(teachers);
    }

    @GetMapping(value = "/{course_id}/guarantors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Guarantor>> getCourseGuarantors(@PathVariable Integer course_id){
        Course course = courseService.findCourseById(course_id);
        List<Guarantor> guarantors = courseService.getCourseGuarantors(course);
        if (guarantors == null){
            LOG.error("Guarantors of course {} were not found.", course.getName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Guarantors of course {} were found.", course.getName());
        return ResponseEntity.ok(guarantors);
    }

    @GetMapping(value = "/{course_id}/teacher/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Teacher> getTeacherByName(@PathVariable String name, @PathVariable String course_id){
        Teacher teacher = courseService.getTeacherByName(name);
        if (teacher == null){
            LOG.error("Teacher {} was not found.", name);
            return ResponseEntity.notFound().build();
        }
        LOG.info("Teacher {} was found.", name);
        return ResponseEntity.ok(teacher);
    }


    @PostMapping(value = "/{course_id}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> enrollStudent(@PathVariable Integer course_id){
        try {
            Course course = courseService.findCourseById(course_id);
            SecurityContextHolder.getContext().getAuthentication();
            Student student = (Student) ((SISUserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser();
            return ResponseEntity.ok(studentService.addCourseToPlan(course, student));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{course_id}/unroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> unrollStudent(@PathVariable Integer course_id){
        try {
            Course course = courseService.findCourseById(course_id);
            SecurityContextHolder.getContext().getAuthentication();
            Student student = (Student) ((SISUserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser();
            return ResponseEntity.ok(studentService.removeCourseFromPlan(course, student));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('GUARANTOR')")
    @PutMapping(value = "/editCourse/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> editCourse(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try{
            String name = body.get("name");
            String description = body.get("description");
            Course course = courseService.findCourseById(id);
            String oldName = course.getName();
            if (course == null){
                LOG.error("Course {} was not found.", name);
                return ResponseEntity.notFound().build();
            }
            courseService.editCourse(id, name, description);
            courseService.update(course);
            if (!oldName.equals(name)){
                LOG.info("Course {} was renamed to {}.", oldName, name);
            }
            LOG.info("Course {} was edited.", course.getName());
            return ResponseEntity.ok(new CourseDTO(course));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('GUARANTOR')")
    @GetMapping(value = "/{course_id}/enrollments", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<EnrollmentDTO>> getEnrollments(@PathVariable Integer course_id){
        Course course = courseService.findCourseById(course_id);
        List<EnrollmentRecord> enrollments = courseService.getActualEnrollments(course);
        if (enrollments == null){
            LOG.error("Enrollments of course {} were not found.", course.getName());
            return ResponseEntity.notFound().build();
        }
        LOG.info("Enrollments of course {} were found.", course.getName());
        return ResponseEntity.ok(enrollments.stream().map(EnrollmentDTO::new).toList());
    }
}
