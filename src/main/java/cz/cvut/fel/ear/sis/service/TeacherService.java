package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.TeacherDao;
import cz.cvut.fel.ear.sis.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {
    private final TeacherDao teacherDao;

    @Autowired
    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Transactional
    public Teacher findTeacherById(Integer teacherId) {
        return teacherDao.find(teacherId);
    }
}
