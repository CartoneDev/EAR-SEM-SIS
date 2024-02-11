package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.EnrollmentRecordDao;
import cz.cvut.fel.ear.sis.DAO.StudentDao;
import cz.cvut.fel.ear.sis.DAO.CourseDao;
import cz.cvut.fel.ear.sis.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {
    private EnrollmentRecordDao enrollmentRecordDao;
    private CourseService courseService;

    private StudentDao studentDao;

    @Autowired
    public StudentService(EnrollmentRecordDao enrollmentRecordDao, CourseService courseService, StudentDao studentDao) {
        this.enrollmentRecordDao = enrollmentRecordDao;
        this.courseService = courseService;
        this.studentDao = studentDao;
    }

    @Transactional
    public void addCourseToPlan(Course course, Student student){
        courseService.checkPrerequisites(course, student);
        addCourse(course, student);
    }

    @Transactional
    public void addCourseToPlanWithoutPreqs(Course course, Student student, Admin invoker){
        Objects.requireNonNull(invoker);
        addCourse(course, student);
    }

    private void addCourse(Course course, Student student) {
        EnrollmentRecord enrollmentRecord = new EnrollmentRecord();
        enrollmentRecord.setCourse(course);
        enrollmentRecord.setStudent(student);
        enrollmentRecord.setSemYear(getCurrentSemYear());
        student.getEnrollmentRecords().add(enrollmentRecord);
        enrollmentRecordDao.persist(enrollmentRecord);
    }

    private String getCurrentSemYear(){
        Year currentYear = Year.now();
        int currentSemester = 1;
        String semesterLabel = currentSemester == 1 ? "ZS" : "LS";
        int nextYear = currentYear.getValue() + 1;
        String yearLabel = currentSemester == 1 ? "/" + nextYear : "/" + currentYear;
        return semesterLabel + yearLabel;
    }

    @Transactional
    public void removeCourseFromPlan(Course course, Student student) {
        EnrollmentRecord enrollmentRecord = enrollmentRecordDao
                .findByStudentIdAndCourseId(student.getId(), course.getId());
        if (enrollmentRecord == null){
            throw new IllegalArgumentException("Student is not enrolled in this course.");
        }
        enrollmentRecordDao.remove(enrollmentRecord);
    }

    @Transactional
    public Student getStudentById(int id) {
        return studentDao.find(id);
    }

    @Transactional
    public List<Schedule> getSchedule(Student student){
        return studentDao.find(student.getId()).getSchedules();
    }

    public Student findStudentById(int studentId) {
        return studentDao.find(studentId);
    }
}
