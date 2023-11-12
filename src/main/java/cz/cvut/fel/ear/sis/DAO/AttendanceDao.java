package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Attendance;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceDao extends BaseDao<Attendance>{
    public AttendanceDao() {super(Attendance.class); }
}
