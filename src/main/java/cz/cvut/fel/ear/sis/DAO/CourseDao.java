package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Program;
import cz.cvut.fel.ear.sis.model.Attendance;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CourseDao extends BaseDao<Course> {
    public CourseDao() {super(Course.class); }

    public ArrayList<Course> findAll(Program program) {
        return new ArrayList<>(program.getAttendances().stream().map(Attendance::getCourse).filter(Objects::nonNull).toList());
    }
}
