package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.AdminDao;
import cz.cvut.fel.ear.sis.DAO.StudentDao;
import cz.cvut.fel.ear.sis.DAO.TeacherDao;
import cz.cvut.fel.ear.sis.DAO.UserDao;
import cz.cvut.fel.ear.sis.exception.UnAuthorizedException;
import cz.cvut.fel.ear.sis.model.Admin;
import cz.cvut.fel.ear.sis.model.Student;
import cz.cvut.fel.ear.sis.model.Teacher;
import cz.cvut.fel.ear.sis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final AdminDao adminDao;
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserDao userDao, StudentDao studentDao, AdminDao adminDao, TeacherDao teacherDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.adminDao = adminDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void persist(User toAdd, User invoker){
        toAdd.encodePassword(passwordEncoder);
        if (invoker instanceof Admin){
            throw new UnAuthorizedException("");
        }
        userDao.persist(toAdd); // ideally, userdao since it's single table should cover all scenarios
    }
    @Transactional
    public void update(User toSet, User invoker){
        if (invoker instanceof Admin){
            throw new UnAuthorizedException("");// there should be a better way to handle authorization
        }
        userDao.update(toSet);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public <T>List<T> findAll( Class<T> c) { // maybe generalize it all to userdao?
        if (c.equals(Student.class)){
            return (List<T>) studentDao.findAll();
        } else if (c.equals(Teacher.class)) {
            return (List<T>) teacherDao.findAll();
        } else if (c.equals(Admin.class)) {
            return (List<T>) adminDao.findAll();
        }
        return null;
    }
    @Transactional
    public void remove(User toRemove, User invoker){
        if (invoker instanceof Admin){
            throw new UnAuthorizedException("");
        }
        userDao.persist(toRemove); // ideally, userdao since it's single table should cover all scenarios
    }

    public boolean exists(String userName) {
        return userDao.findAll().stream().anyMatch(user -> user.getUsername().equals(userName));
    }
}
