package cz.cvut.fel.ear.sis.service;


import cz.cvut.fel.ear.sis.DAO.BaseDao;
import cz.cvut.fel.ear.sis.DAO.CourseDao;
import cz.cvut.fel.ear.sis.DAO.EnrollmentRecordDao;
import cz.cvut.fel.ear.sis.exception.PrerequisiteException;
import cz.cvut.fel.ear.sis.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    private final CourseDao courseDao;
    private final EnrollmentRecordDao enrollmentRecordDao;

    public CourseService(CourseDao courseDao, EnrollmentRecordDao enrollmentRecordDao){
        this.courseDao = courseDao;
        this.enrollmentRecordDao = enrollmentRecordDao;
    }

    public List<Course> listStudentCourses(Student student){
        Set<Course> courses = new HashSet<>();
        for (EnrollmentRecord e: student.getEnrollmentRecords()) {
            courses.add(e.getCourse());
        }
        return courses.stream().toList();
    }

    public List<Course> listStudentCurrentCourses(Student student){
        Set<Course> courses = new HashSet<>();
        for (EnrollmentRecord e: student.getEnrollmentRecords()) {
            if (e.getGrade() == GradeType.UNGRADED){
                courses.add(e.getCourse());
            }
        }
        return courses.stream().toList();
    }

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

        for (Prerequisite pre: prerequisites) {
          if (!studentCourses.contains(pre.getPrerequisiteCourse())){
              throw new PrerequisiteException("Course " + pre.getPrerequisiteCourse().getName() + " is not absolved");
          }
        }
    }


    public void gradeStudent(Course course, Student student, Guarantor invoker, GradeType grade){
        EnrollmentRecord er =
        student.getEnrollmentRecords().stream().filter(c -> c.getCourse().getId().equals(course.getId()))
                .filter(enrollmentRecord -> enrollmentRecord.getGrade().equals(GradeType.UNGRADED)).toList().get(0);
        if (er != null){
            er.setGrade(grade);
            enrollmentRecordDao.update(er);
        } else {
            throw new RuntimeException("Student is not zapisan to " + course.getName());
        }
    }

    public List<Teacher> getCourseTeachers(Course course){
        return course.getTeachers();
    }
    @Transactional
    public void persist(Course course){
        courseDao.persist(course);
    }

    @Transactional
    public void update(Course course){
        courseDao.update(course);
    }



}
