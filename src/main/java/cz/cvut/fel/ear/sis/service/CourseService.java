package cz.cvut.fel.ear.sis.service;


import cz.cvut.fel.ear.sis.DAO.BaseDao;
import cz.cvut.fel.ear.sis.DAO.CourseDao;
import cz.cvut.fel.ear.sis.DAO.EnrollmentRecordDao;
import cz.cvut.fel.ear.sis.DAO.TeacherDao;
import cz.cvut.fel.ear.sis.exception.PrerequisiteException;
import cz.cvut.fel.ear.sis.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CourseService {
    private final CourseDao courseDao;
    private final EnrollmentRecordDao enrollmentRecordDao;
    private final TeacherDao teacherDao;

    @Autowired
    public CourseService(CourseDao courseDao, EnrollmentRecordDao enrollmentRecordDao, TeacherDao teacherDao) {
        this.courseDao = courseDao;
        this.enrollmentRecordDao = enrollmentRecordDao;
        this.teacherDao = teacherDao;
    }

    @Transactional
    public List<Course> listStudentCourses(Student student) {
        Set<Course> courses = new HashSet<>();
        for (EnrollmentRecord e : student.getEnrollmentRecords()) {
            courses.add(e.getCourse());
        }
        return courses.stream().toList();
    }

    @Transactional
    public List<Course> listStudentCurrentCourses(Student student) {
        Set<Course> courses = new HashSet<>();
        for (EnrollmentRecord e : student.getEnrollmentRecords()) {
            if (e.getGrade() == GradeType.UNGRADED) {
                courses.add(e.getCourse());
            }
        }
        return courses.stream().toList();
    }

    @Transactional
    public void checkPrerequisites(Course course, Student student) {
        List<Prerequisite> prerequisites = course
                .getPrerequisites()
                .stream()
                .filter(Prerequisite::getIsMandatory)
                .toList();
        List<Course> studentCourses = student
                .getEnrollmentRecords()
                .stream()
                .filter(enrollmentRecord -> enrollmentRecord.getGrade() != GradeType.UNGRADED &&
                        enrollmentRecord.getGrade() != GradeType.F)
                .map(EnrollmentRecord::getCourse).toList();
        // Nu i hujnya

        for (Prerequisite pre : prerequisites) {
            if (!studentCourses.contains(pre.getPrerequisiteCourse())) {
                throw new PrerequisiteException("Course " + pre.getPrerequisiteCourse().getName() + " is not absolved");
            }
        }
    }

    @Transactional
    public void gradeStudent(Course course, Student student, Guarantor invoker, GradeType grade) {
        EnrollmentRecord er =
                student.getEnrollmentRecords().stream().filter(c -> c.getCourse().getId().equals(course.getId()))
                        .filter(enrollmentRecord -> enrollmentRecord.getGrade().equals(GradeType.UNGRADED)).toList().get(0);
        if (er != null) {
            er.setGrade(grade);
            enrollmentRecordDao.update(er);
        } else {
            throw new RuntimeException("Student is not zapisan to " + course.getName());
        }
    }

    @Transactional
    public List<Teacher> getCourseTeachers(Course course) {
        return course.getTeachers();
    }

    @Transactional
    public void persist(Course course) {
        Objects.requireNonNull(course);
        courseDao.persist(course);
    }

    @Transactional
    public void update(Course course) {
        courseDao.update(course);
    }

    @Transactional
    public Course findCourseById(int id) {
        return courseDao.find(id);
    }

    @Transactional
    public List<Course> findAllCourses() {
        return courseDao.findAll();
    }

    @Transactional
    public Course findCourseByName(String name) {
        return courseDao.findCourseByName(name);
    }

    @Transactional
    public void addTeacherToCourse(Course course, Teacher teacher) {
        course.getTeachers().add(teacher);
    }

    @Transactional
    public void removeTeacherFromCourse(Course course, Teacher teacher) {
        course.getTeachers().remove(teacher); // Чтак можно или низя???????
    }

    @Transactional
    public void addPrerequisite(Course course, Course prerequisite) {
        Prerequisite pre = new Prerequisite();
        pre.setPrerequisiteCourse(course);
        pre.setPrerequisiteCourse(prerequisite);
        pre.setIsMandatory(true);
        course.getPrerequisites().add(pre);
    }

    @Transactional
    public void removePrerequisite(Course course, Course prerequisite) {
        Prerequisite pre = course.getPrerequisites().stream().filter(p -> p.getPrerequisiteCourse().getId().equals(prerequisite.getId())).toList().get(0);
        course.getPrerequisites().remove(pre);
    }


    public Course makeCourse(String coursename, String desc, Integer credits, Integer hoursLecture, Integer hoursPractise, Guarantor guarantor) {
        Course course = courseDao.findCourseByName(coursename);
        if (course != null) {
            return course;
        }
        course = new Course();
        course.setName(coursename);
        course.setDescription(desc);
        course.setCredits(credits);
        course.setHoursLecture(hoursLecture);
        course.setHoursPractise(hoursPractise);
        course.setGuarantor(guarantor);
        courseDao.persist(course);
        return course;
    }
}
