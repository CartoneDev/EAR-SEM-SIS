package cz.cvut.fel.ear.sis;

import cz.cvut.fel.ear.sis.DAO.UserDao;
import cz.cvut.fel.ear.sis.model.Admin;
import cz.cvut.fel.ear.sis.model.Guarantor;
import cz.cvut.fel.ear.sis.model.Teacher;
import cz.cvut.fel.ear.sis.model.User;
import cz.cvut.fel.ear.sis.service.CourseService;
import cz.cvut.fel.ear.sis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DummyData implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    final
    UserService userService;

    final CourseService courseService;
    public DummyData(PasswordEncoder passwordEncoder, UserService userService, CourseService courseService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void run(String... args) throws Exception {
//        courseService.generateIndividualProgram();

        userService.makeUserNamed("admin", "admin", new Admin());
        userService.makeUserNamed("user", "user", new User());
        Guarantor teacher = (Guarantor) userService.makeUserNamed("MathTeacher", "teacher", new Guarantor());
        courseService.makeCourse("Math", "Mathematics", 5, 4,4, teacher);
//        userService.makeUserNamed("teacher", "teacher", new Teacher());
    }

}
