package cz.cvut.fel.ear.sis.service;


import cz.cvut.fel.ear.sis.DAO.*;
import cz.cvut.fel.ear.sis.DTO.CourseDTO;
import cz.cvut.fel.ear.sis.exception.PrerequisiteException;
import cz.cvut.fel.ear.sis.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseService {
    private final CourseDao courseDao;
    private final EnrollmentRecordDao enrollmentRecordDao;
    private final TeacherDao teacherDao;

    private final PrerequisiteDao prerequisiteDao;

    private final GuarantorDao guarantorDao;

    @Autowired
    public CourseService(CourseDao courseDao, EnrollmentRecordDao enrollmentRecordDao,
                         TeacherDao teacherDao, PrerequisiteDao prerequisiteDao, GuarantorDao guarantorDao) {
        this.courseDao = courseDao;
        this.enrollmentRecordDao = enrollmentRecordDao;
        this.teacherDao = teacherDao;
        this.prerequisiteDao = prerequisiteDao;
        this.guarantorDao = guarantorDao;
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
            if (e.getGrade() == GradeType.UNGRADED || e.getGrade() == null) {
                courses.add(e.getCourse());
            }
        }
        return courses.stream().toList();
    }

    @Transactional
    public void checkPrerequisites(Course course, Student student) {


        List<Prerequisite> prerequisites = prerequisiteDao.findPrerequisitesByCourseId(course.getId());
        List<Course> studentCourses = new ArrayList<>();
        try{
            studentCourses = student.getEnrollmentRecords().stream().filter(e -> e.getGrade() != GradeType.F && e.getGrade()!= GradeType.UNGRADED && e.getGrade() != null).map(EnrollmentRecord::getCourse).toList();
        }catch (Exception e){
        }

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
            throw new RuntimeException("Student is not signed to " + course.getName());
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
        return courseDao.findCourseById(id);
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
        course.addTeacher(teacher);
        courseDao.update(course);
    }

    @Transactional
    public void removeTeacherFromCourse(Course course, Teacher teacher) {
        course.getTeachers().remove(teacher);
    }

    @Transactional
    public void addPrerequisite(Course course, Course prerequisite) {
        Prerequisite pre = new Prerequisite();
        pre.setRequestedCourse(course);
        pre.setPrerequisiteCourse(prerequisite);
        pre.setIsMandatory(true);
        course.getPrerequisites().add(pre);
    }

    @Transactional
    public void removePrerequisite(Course course, Course prerequisite) {
        Prerequisite pre = course.getPrerequisites().stream().filter(p -> p.getPrerequisiteCourse().getId().equals(prerequisite.getId())).toList().get(0);
        course.getPrerequisites().remove(pre);
    }


    @Transactional
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

    public List<Schedule> getCourseSchedule(Course course) {
        return course.getSchedules();
    }

    @Transactional
    public void addScheduleLecture(Guarantor teacher, Course course, LocalDateTime from, LocalDateTime to) {
        Schedule schedule = new Schedule();
        schedule.setFrom(from);
        schedule.setTo(to);
        schedule.setTeacher(teacher);
        schedule.setCourse(course);

        schedule.setLectureType(LectureType.LECTURE);
        schedule.setCapacity(20);
//        scheduleDao.persist(schedule);
    }

    @Transactional
    public Teacher getTeacherByName(String name) {
        return teacherDao.findTeacherByName(name);
    }

    @Transactional
    public List<Guarantor> getCourseGuarantors(Course course) {
        return guarantorDao.findCourseGuarantors(course);
    }

    @Transactional
    public void editCourse(Integer id, String name, String description) {
        Course course = courseDao.findCourseById(id);
        course.setName(name);
        course.setDescription(description);
        courseDao.update(course);
    }

    public List<CourseDTO> getAllCoursesOfStudent(Student student) {
        return student.getEnrollmentRecords().stream().map(EnrollmentRecord::getCourse).map(CourseDTO::new).toList();
    }

    public List<EnrollmentRecord> getActualEnrollments(Course course) {
        return course.getEnrollmentRecords().stream().filter(e -> e.getGrade() == GradeType.UNGRADED).toList();
    }
}
