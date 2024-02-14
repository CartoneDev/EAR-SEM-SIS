package cz.cvut.fel.ear.sis;

import cz.cvut.fel.ear.sis.model.*;
import cz.cvut.fel.ear.sis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DummyData implements CommandLineRunner {

    final
    UserService userService;

    final CourseService courseService;
    private final ScheduleRoomService scheduleRoomService;
    private final ProgramService programService;

    private final AttendanceService attendanceService;
    @Autowired
    public DummyData(UserService userService, CourseService courseService, ScheduleRoomService scheduleRoomService, ProgramService programService, AttendanceService attendanceService) {
        this.userService = userService;
        this.courseService = courseService;
        this.scheduleRoomService = scheduleRoomService;
        this.programService = programService;
        this.attendanceService = attendanceService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void run(String... args) throws Exception {
//        courseService.generateIndividualProgram();
        if (!courseService.findAllCourses().isEmpty()) {
            return;
        }

        Program program = new Program();
        program.setName("Informatika");
        program.setDescription("Informatika is the best");
        program.setAttendances(new ArrayList<>());

        Admin admin = (Admin) userService.makeUserNamed("admin", "admin", new Admin());

        ArrayList<User> students = new ArrayList<>();
        students.add(userService.makeUserNamed("novak", "password", new Student()));
        students.add(userService.makeUserNamed("svoboda", "password", new Student()));
        students.add(userService.makeUserNamed("kovar", "password", new Student()));
        students.add(userService.makeUserNamed("vlcek", "password", new Student()));
        students.add(userService.makeUserNamed("prochazka", "password", new Student()));
        students.add(userService.makeUserNamed("urban", "password", new Student()));
        students.add(userService.makeUserNamed("benes", "password", new Student()));
        students.add(userService.makeUserNamed("kadlec", "password", new Student()));
        students.add(userService.makeUserNamed("marek", "password", new Student()));
        students.add(userService.makeUserNamed("dvorak", "password", new Student()));
        students.add(userService.makeUserNamed("hajek", "password", new Student()));
        students.add(userService.makeUserNamed("sedlak", "password", new Student()));
        students.add(userService.makeUserNamed("pospisil", "password", new Student()));
        students.add(userService.makeUserNamed("jelinek", "password", new Student()));
        students.add(userService.makeUserNamed("ruzicka", "password", new Student()));
        students.add(userService.makeUserNamed("hruska", "password", new Student()));
        students.add(userService.makeUserNamed("sikora", "password", new Student()));
        students.add(userService.makeUserNamed("holub", "password", new Student()));
        students.add(userService.makeUserNamed("liska", "password", new Student()));
        students.add(userService.makeUserNamed("svobodova", "password", new Student()));

        program.setStudents(students.stream().map(s -> (Student) s).toList());
        students.forEach(s -> {
            ((Student)s).setProgram(program);
            userService.update(s, admin);
        });
        programService.persist(program);

        Guarantor matGuarantor = (Guarantor) userService.makeUserNamed("phdthakur", "guarantor", new Guarantor());

        Course mat1 = courseService.makeCourse("Matematika 1", "zaklady matematiky", 5, 4, 4, matGuarantor);
        matGuarantor = (Guarantor) userService.makeUserNamed("phdeinstain", "guarantor", new Guarantor());
        Course mat2 = courseService.makeCourse("Matematika 2", "pokrocile matematicke koncepty", 6, 3, 4, matGuarantor);
        matGuarantor = (Guarantor) userService.makeUserNamed("phdkovalevskaja", "guarantor", new Guarantor());
        Course mat3 = courseService.makeCourse("Matematika 3", "diferencialni rovnice", 7, 3, 5, matGuarantor);
        ArrayList<Course> subjects = new ArrayList<>();
        subjects.add(mat1);
        subjects.add(mat2);
        subjects.add(mat3);
        ArrayList<Teacher> teachers = new ArrayList<>();

        teachers.add((Teacher) userService.makeUserNamed("ingNovakTeacher", "teacher", new Teacher()));
        teachers.add((Teacher) userService.makeUserNamed("ingSvobodaTeacher", "teacher", new Teacher()));
        teachers.add((Teacher) userService.makeUserNamed("ingKovarTeacher", "teacher", new Teacher()));

        mat1.addTeacher(teachers.get(0));
        mat1.addTeacher(teachers.get(1));
        mat2.addTeacher(teachers.get(1));
        mat2.addTeacher(teachers.get(2));
        mat3.addTeacher(teachers.get(2));
        mat3.addTeacher(teachers.get(0));

        Attendance attendance = new Attendance();
        attendance.setCourse(mat1);
        attendance.setProgram(program);
        attendance.setAttendanceType(AttendanceType.MANDATORY);
        attendanceService.persist(attendance);
        attendance = new Attendance();
        attendance.setCourse(mat2);
        attendance.setProgram(program);
        attendance.setAttendanceType(AttendanceType.MANDATORY);
        attendanceService.persist(attendance);
        attendance = new Attendance();
        attendance.setCourse(mat3);
        attendance.setProgram(program);
        attendance.setAttendanceType(AttendanceType.MANDATORY);
        attendanceService.persist(attendance);

        courseService.addPrerequisite(mat2, mat1);
        courseService.update(mat2);
        courseService.addPrerequisite(mat3, mat2);
        courseService.update(mat3);

        LocalDateTime from = (LocalDateTime.parse("2024-02-01T10:00:00"));
        LocalDateTime to = (LocalDateTime.parse("2024-02-01T12:00:00"));
        String room = "B-108";
        scheduleRoomService.addScheduleLecture(teachers.get(0), mat1, room, from, to);

        LocalDateTime from2 = LocalDateTime.parse("2024-02-02T13:00:00");
        LocalDateTime to2 = LocalDateTime.parse("2024-02-02T15:00:00");
        String room2 = "Auditorium-A";
        scheduleRoomService.addScheduleLecture(teachers.get(1), subjects.get(1), room2, from2, to2);

        LocalDateTime from3 = LocalDateTime.parse("2024-02-03T16:00:00");
        LocalDateTime to3 = LocalDateTime.parse("2024-02-03T18:00:00");
        String room3 = "B-109";
        scheduleRoomService.addScheduleLecture(teachers.get(2), subjects.get(2), room3, from3, to3);

        // Additional entries
        LocalDateTime from4 = LocalDateTime.parse("2024-02-04T09:30:00");
        LocalDateTime to4 = LocalDateTime.parse("2024-02-04T11:30:00");
        String room4 = "C-201";
        scheduleRoomService.addScheduleLecture(teachers.get(0), subjects.get(0), room4, from4, to4);

        LocalDateTime from5 = LocalDateTime.parse("2024-02-05T14:00:00");
        LocalDateTime to5 = LocalDateTime.parse("2024-02-05T16:00:00");
        String room5 = "Auditorium-B";
        scheduleRoomService.addScheduleLecture(teachers.get(1), subjects.get(1), room5, from5, to5);

        LocalDateTime from6 = LocalDateTime.parse("2024-02-06T12:30:00");
        LocalDateTime to6 = LocalDateTime.parse("2024-02-06T14:30:00");
        String room6 = "D-105";
        scheduleRoomService.addScheduleLecture(teachers.get(2), subjects.get(2), room6, from6, to6);

        LocalDateTime from7 = LocalDateTime.parse("2024-02-07T15:30:00");
        LocalDateTime to7 = LocalDateTime.parse("2024-02-07T17:30:00");
        String room7 = "B-110";
        scheduleRoomService.addScheduleLecture(teachers.get(0), subjects.get(0), room7, from7, to7);

        LocalDateTime from8 = LocalDateTime.parse("2024-02-08T11:00:00");
        LocalDateTime to8 = LocalDateTime.parse("2024-02-08T13:00:00");
        String room8 = "Auditorium-C";
        scheduleRoomService.addScheduleLecture(teachers.get(1), subjects.get(1), room8, from8, to8);

        LocalDateTime from9 = LocalDateTime.parse("2024-02-09T16:30:00");
        LocalDateTime to9 = LocalDateTime.parse("2024-02-09T18:30:00");
        String room9 = "C-202";
        scheduleRoomService.addScheduleLecture(teachers.get(2), subjects.get(2), room9, from9, to9);

        LocalDateTime from10 = LocalDateTime.parse("2024-02-10T09:00:00");
        LocalDateTime to10 = LocalDateTime.parse("2024-02-10T11:00:00");
        String room10 = "D-106";
        scheduleRoomService.addScheduleLecture(teachers.get(0), subjects.get(0), room10, from10, to10);
    }

}
