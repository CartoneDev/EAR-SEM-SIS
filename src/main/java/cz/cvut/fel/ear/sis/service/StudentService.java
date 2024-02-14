package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.EnrollmentRecordDao;
import cz.cvut.fel.ear.sis.DAO.StudentDao;
import cz.cvut.fel.ear.sis.DAO.CourseDao;
import cz.cvut.fel.ear.sis.DTO.CourseDTO;
import cz.cvut.fel.ear.sis.DAO.UserDao;
import cz.cvut.fel.ear.sis.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
    private UserDao userDao;

    private final EntityManager em;

    @Autowired
    public StudentService(EnrollmentRecordDao enrollmentRecordDao, CourseService courseService, StudentDao studentDao, UserDao userDao, EntityManager em) {
        this.enrollmentRecordDao = enrollmentRecordDao;
        this.courseService = courseService;
        this.studentDao = studentDao;
        this.userDao = userDao;
        this.em = em;
    }

    @Transactional
    public String addCourseToPlan(Course course, Student student){
        System.out.println("Adding course to plan");
        if (student.getEnrollmentRecords().stream().anyMatch(e -> Objects.equals(e.getCourse().getId(), course.getId()) && e.getSemYear().equals(getCurrentSemYear()))){
            return "Course is already in student's plan.";
        }
        courseService.checkPrerequisites(course, student);
        addCourse(course, student);
        return "Course added to plan.";
    }

    @Transactional
    public void addCourseToPlanWithoutPreqs(Course course, Student student, Admin invoker){
        Objects.requireNonNull(invoker);
        addCourse(course, student);
        student.addCourse(course);
    }

    private void addCourse(Course course, Student student) {
        EnrollmentRecord enrollmentRecord = new EnrollmentRecord();
        enrollmentRecord.setCourse(course);
        enrollmentRecord.setStudent(student);
        enrollmentRecord.setSemYear(getCurrentSemYear());
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
    public String removeCourseFromPlan(Course course, Student student) {
        EnrollmentRecord enrollmentRecord = enrollmentRecordDao
                .findByStudentIdAndCourseId(student.getId(), course.getId());
        if (enrollmentRecord == null){
            throw new IllegalArgumentException("Student is not enrolled in this course.");
        }
        enrollmentRecordDao.remove(enrollmentRecord);
        return "Course removed from plan.";
    }

    @Transactional
    public Student getStudentById(int id) {
        return studentDao.find(id);
    }

    @Transactional
    public List<Schedule> getSchedule(Student student){
        return studentDao.find(student.getId()).getSchedules();
    }

    @Transactional
    public Student findStudentById(int studentId) {
        return studentDao.find(studentId);
    }

    @Transactional
    public List<Student> findAllStudents() {
        return studentDao.findAll();
    }

    @Transactional
    public List<CourseDTO> getAllCoursesOfStudent(Student student) {
        return courseService.getAllCoursesOfStudent(student);
    }
    @Transactional
    public Student findStudentByUsername(Object principal) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Student> cq = cb.createQuery(Student.class);
            return em.createQuery(cq.select(cq.from(Student.class)).where(cb.equal(cq.from(User.class).get("username"), principal.toString()))).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
