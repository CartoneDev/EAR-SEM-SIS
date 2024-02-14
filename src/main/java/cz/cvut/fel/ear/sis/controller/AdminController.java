package cz.cvut.fel.ear.sis.controller;

import cz.cvut.fel.ear.sis.DTO.CourseDTO;
import cz.cvut.fel.ear.sis.DTO.ScheduleDTO;
import cz.cvut.fel.ear.sis.DTO.UserDTO;
import cz.cvut.fel.ear.sis.model.*;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.ScheduleRoomService;
import cz.cvut.fel.ear.sis.service.StudentService;
import cz.cvut.fel.ear.sis.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ScheduleRoomService scheduleService;

    public AdminController(CourseService courseService, TeacherService teacherService, StudentService studentService, ScheduleRoomService scheduleService){
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
    }

    @PostMapping(value = "/addTeacher", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> addTeacherToCourse(@RequestBody Map<String, String> body ) {
        try {
            int course_id = Integer.parseInt(body.get("course_id"));
            int teacher_id = Integer.parseInt(body.get("teacher_id"));
            Course course = courseService.findCourseById(course_id);
            Teacher teacher = teacherService.findTeacherById(teacher_id);
            if (course == null || teacher == null){
                LOG.error("Course {} or teacher {} was not found.", course_id, teacher_id);
                return ResponseEntity.notFound().build();
            }
            courseService.addTeacherToCourse(course, teacher);
            LOG.info("Teacher {} was added to course {}.", teacher.getFirstName(), course.getName());
            return ResponseEntity.ok(new CourseDTO(course));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/removeTeacher", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> removeTeacherFromCourse(@RequestBody Map<String, String> body ) {
        try{
            int course_id = Integer.parseInt(body.get("course_id"));
            int teacher_id = Integer.parseInt(body.get("teacher_id"));
            Course course = courseService.findCourseById(course_id);
            Teacher teacher = teacherService.findTeacherById(teacher_id);
            if (course == null || teacher == null){
                LOG.error("Course {} or teacher {} was not found.", course_id, teacher_id);
                return ResponseEntity.notFound().build();
            }
            courseService.removeTeacherFromCourse(course, teacher);
            LOG.info("Teacher {} was removed from course {}.", teacher.getFirstName(), course.getName());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping(value="/students", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students.stream().map(UserDTO::new).toList());
    }

    @PostMapping(value = "/addCourseToStudent/{student_id}/{course_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> addCourseToStudent(@PathVariable int course_id,@PathVariable int student_id) {
        try {
            Course course = courseService.findCourseById(course_id);
            Student student = studentService.findStudentById(student_id);
            if (course == null || student == null) {
                LOG.error("Course {} or student {} was not found.", course_id, student_id);
                return ResponseEntity.notFound().build();
            }
            Admin admin = (Admin) ((SISUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser();
            studentService.addCourseToPlanWithoutPreqs(course, student, admin);
            LOG.info("Course {} was added to plan of student {}.", course.getName(), student.getFirstName());
            return ResponseEntity.ok(new CourseDTO(course));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/removeCourseFromStudent/{student_id}/{course_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeCourseFromStudent(@PathVariable int course_id,@PathVariable int student_id) {
        try {
            Course course = courseService.findCourseById(course_id);
            Student student = studentService.findStudentById(student_id);
            if (course == null || student == null) {
                LOG.error("Course {} or student {} was not found.", course_id, student_id);
                return ResponseEntity.notFound().build();
            }
            studentService.removeCourseFromPlan(course, student);
            LOG.info("Course {} was removed from plan of student {}.", course.getName(), student.getFirstName());
            return ResponseEntity.ok("Course removed from plan.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/students/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getStudentByUsername(@PathVariable String username) {
        try {
            Student student = studentService.findStudentByUsername(username);
            if (student == null) {
                LOG.error("Student {} was not found.", username);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new UserDTO(student));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/schedules", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleDTO> setSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try {
            Schedule schedule = new Schedule();
            schedule.setFrom(scheduleDTO.getFrom());
            schedule.setTo(scheduleDTO.getTo());
            schedule.setLectureType(scheduleDTO.getLectureType());
            schedule.setCapacity(scheduleDTO.getCapacity());

            Course course = new Course();
            course.setId(scheduleDTO.getCourse().id);

            schedule.setCourse(course);

            Teacher teacher = teacherService.findTeacherById(scheduleDTO.getTeacher());
            schedule.setTeacher(teacher);

            Room room = new Room();
            room.setId(scheduleDTO.getRoom().getId());

            schedule.setRoom(room);

            scheduleService.save(schedule);

            return ResponseEntity.ok(new ScheduleDTO(schedule));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
