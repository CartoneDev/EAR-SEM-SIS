package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.exception.PrerequisiteException;
import cz.cvut.fel.ear.sis.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class StudentServiceTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private StudentService studentService;
    private final Random random = new Random();

    @Test
    @Transactional
    public void testStudentCanAddCourse(){
        Course course = generateCourse();
        Program program = generateProgram("Test");
        em.persist(program);
        Student student = generateStudent(program);
        em.persist(course);
        em.persist(student);

        studentService.addCourseToPlan(course,student);
        student = em.find(student.getClass(), student.getId());
        assertTrue(student.getEnrollmentRecords().stream().map(EnrollmentRecord::getCourse).map(Course::getId).anyMatch(id -> id==course.getId()));
    }

    @Test
    @Transactional
    public void testStudentCannotAddCourseWithPrerequisite(){
        Course requiredCourse = generateCourse();
        em.persist(requiredCourse);
        Prerequisite prerequisite = new Prerequisite();
        prerequisite.setIsMandatory(Boolean.TRUE);
        prerequisite.setRequestedCourse(requiredCourse);
        Course course = generateCourse();
        Program program = generateProgram("Test");
        em.persist(program);
        Student student = generateStudent(program);
        em.persist(course);
        em.persist(student);
        prerequisite.setPrerequisiteCourse(course);
        ArrayList <Prerequisite> ar = new ArrayList<>();
        ar.add(prerequisite);
        course.setPrerequisites(ar);
        em.persist(course);

        assertThrows(PrerequisiteException.class, ()->studentService.addCourseToPlan(course,student));
    }
    private Course generateCourse() {
        Course course = new Course();
        course.setName("Course:" +  random.nextInt());
        course.setDescription("desc");
        course.setCredits(3);
        course.setHoursLecture(1);
        course.setHoursPractise(1);
        return course;
    }
    private static Program generateProgram(String name) {
        final Program prog = new Program();
        prog.setName(name);
        prog.setDescription("Magnificent program");
        return prog;
    }

    private Student generateStudent(Program program){
        Student student = new Student();
        student.setPassword("");
        student.encodePassword(new BCryptPasswordEncoder());
        student.setUsername("uname" + random.nextInt());
        student.setFirstName("Jonny");
        student.setLastName("Hurt");
        student.setProgram(program);
        student.setEmail("");
        student.setStudentNumber(String.valueOf(random.nextInt()));
        return student;
    }
}
