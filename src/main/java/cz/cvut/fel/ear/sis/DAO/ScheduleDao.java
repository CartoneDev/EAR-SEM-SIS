package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Schedule;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDao extends BaseDao<Schedule>{
    public ScheduleDao() {super(Schedule.class); }
}
