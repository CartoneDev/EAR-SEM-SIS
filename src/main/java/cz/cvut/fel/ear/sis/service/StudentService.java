package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.EnrollmentRecordDao;
import cz.cvut.fel.ear.sis.DAO.StudentDao;
import cz.cvut.fel.ear.sis.DAO.CourseDao;
import cz.cvut.fel.ear.sis.model.Admin;
import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.EnrollmentRecord;
import cz.cvut.fel.ear.sis.model.Student;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StudentService {
    private EnrollmentRecordDao enrollmentRecordDao;
    private CourseService courseService;

    @Autowired
    public StudentService(EnrollmentRecordDao enrollmentRecordDao, CourseService courseService) {
        this.enrollmentRecordDao = enrollmentRecordDao;
        this.courseService = courseService;
    }


    public void addCourseToPlan(Course course, Student student){
        courseService.checkPrerequisites(course, student);
        addCourse(course, student);
    }

    public void addCourseToPlanWithoutPreqs(Course course, Student student, Admin invoker){
        Objects.requireNonNull(invoker);
        addCourse(course, student);
    }

    private void addCourse(Course course, Student student) {
        EnrollmentRecord enrollmentRecord = new EnrollmentRecord();
        enrollmentRecord.setCourse(course);
        enrollmentRecord.setStudent(student);
        enrollmentRecord.setSemYear(getCurrentSemYear());
        enrollmentRecordDao.persist(enrollmentRecord);
    }


    public String getCurrentSemYear(){
        return "ZS2023/24";
        //TODO dodelat
    }
}
