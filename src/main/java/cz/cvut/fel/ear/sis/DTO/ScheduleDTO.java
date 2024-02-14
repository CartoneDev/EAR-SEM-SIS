package cz.cvut.fel.ear.sis.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import cz.cvut.fel.ear.sis.model.LectureType;
import cz.cvut.fel.ear.sis.model.Room;
import cz.cvut.fel.ear.sis.model.Schedule;
import lombok.Data;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
public class ScheduleDTO {
    private LocalDateTime from;

    private LocalDateTime to;
    private LectureType lectureType;

    private Integer capacity;
    private Integer currentCapacity;
    private CourseDTO course;

    private int teacher;

    private Room room;

    public ScheduleDTO() {
    }

    public ScheduleDTO(Schedule schedule){
        this.from = schedule.getFrom();
        this.to = schedule.getTo();
        this.lectureType = schedule.getLectureType();
        this.capacity = schedule.getCapacity();
        this.currentCapacity = schedule.getStudents().size();
        this.course = new CourseDTO(schedule.getCourse());
        this.teacher = schedule.getTeacher().getId();
        this.room = new RoomDTO(schedule.getRoom());
    }

}
