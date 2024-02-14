package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.AttendanceDao;
import cz.cvut.fel.ear.sis.model.Attendance;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    final
    AttendanceDao attendanceDao;


    public AttendanceService(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }
    public void persist(Attendance attendance) {
        attendanceDao.persist(attendance);
    }
}
