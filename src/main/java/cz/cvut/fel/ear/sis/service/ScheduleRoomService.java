package cz.cvut.fel.ear.sis.service;

import cz.cvut.fel.ear.sis.DAO.RoomDao;
import cz.cvut.fel.ear.sis.DAO.ScheduleDao;
import cz.cvut.fel.ear.sis.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static cz.cvut.fel.ear.sis.model.LectureType.LABORATORY;

@Service
public class ScheduleRoomService {
    final ScheduleDao scheduleDao;
    final RoomDao roomDao;
    @Autowired
    public ScheduleRoomService(ScheduleDao scheduleDao, RoomDao roomDao) {
        this.scheduleDao = scheduleDao;
        this.roomDao = roomDao;
    }

    @Transactional
    public void addScheduleLecture(Teacher teacher, Course course, String roomber, LocalDateTime from, LocalDateTime to) {
        Room room = null;
        try {
            room = roomDao.findByRoomNumber(roomber);
        } catch (Exception e) {
            room = new Room();
            room.setCapacity(20);

            room.setType(RoomType.AUDITORIUM);
            room.setIdentifier(roomber);
            roomDao.persist(room);
        }
//        room = roomDao.findByRoomNumber(roomber);
        Schedule schedule = new Schedule();
        schedule.setFrom(from);
        schedule.setTo(to);
        schedule.setTeacher(teacher);
        schedule.setCourse(course);
        schedule.setRoom(room);
        schedule.setLectureType(LectureType.LECTURE);
        if (Math.random() > 0.25){
            schedule.setLectureType(LABORATORY);
        }
        if (Math.random() > 0.25){
            schedule.setLectureType(LectureType.PRACTISE);
        }
        schedule.setCapacity(20);
        scheduleDao.persist(schedule);
    }

    @Transactional
    public void save(Schedule schedule) {
        scheduleDao.persist(schedule);
    }
}
