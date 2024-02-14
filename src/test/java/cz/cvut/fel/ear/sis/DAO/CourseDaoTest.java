package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.SISApplication;
import cz.cvut.fel.ear.sis.model.Attendance;
import cz.cvut.fel.ear.sis.model.AttendanceType;
import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Program;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ComponentScan(basePackageClasses = SISApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SISApplication.class)})
@ActiveProfiles("test")
public class CourseDaoTest {

    // TestEntityManager provides additional test-related methods (it is Spring-specific)
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CourseDao sut;

    private Random random = new Random();
    @Test
    public void findAllByProgramReturnsCoursesInSpecifiedProgram() {
        final Program program = generateProgram("testCategory");
        em.persist(program);
        final List<Course> courses = generateCourses(program);
        final List<Course> result = sut.findAll(program);
        assertEquals(courses.size(), result.size());
        courses.sort(Comparator.comparing(Course::getName));
        result.sort(Comparator.comparing(Course::getName));
        for (int i = 0; i < courses.size(); i++) {
            assertEquals(courses.get(i).getId(), result.get(i).getId());
        }
    }

    private static Program generateProgram(String name) {
        final Program prog = new Program();
        prog.setName(name);
        prog.setDescription("Magnificent program");
        return prog;
    }

    private List<Course> generateCourses(Program program) {
        final List<Course> courses = new ArrayList<>();
        final List<Attendance> attendances = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Course p = generateCourse();
            em.persist(p);

            // Ensure that the course is associated with a non-null program
            Attendance atd = new Attendance();
            atd.setCourse(p);

            // Set a valid program instance
            atd.setProgram(program);

            atd.setAttendanceType(AttendanceType.VOLUNTARY_FORCED);
            em.persist(atd);
            attendances.add(atd);
            courses.add(p);
        }
        program.setAttendances(attendances);
        em.persist(program);
        return courses;
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

    @Test
    public void testCascadeDeleteAttendance() {
        final Program prog = generateProgram("testProgram");
        em.persist(prog);
        final Course course = generateCourse();
        em.persist(course);

        final Attendance attendance = new Attendance();
        attendance.setAttendanceType(AttendanceType.VOLUNTARY_FORCED);
        attendance.setCourse(course);
        attendance.setProgram(prog);
        em.persist(attendance);

        em.flush();
        em.clear();

        // Now, remove the Course
        Course courseToRemove = em.find(Course.class, course.getId());
        em.remove(courseToRemove);
        em.flush();

        // Check if Attendance is also removed
        assertNull(em.find(Attendance.class, attendance.getId()));
    }

}