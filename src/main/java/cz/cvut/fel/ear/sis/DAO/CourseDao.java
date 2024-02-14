package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Guarantor;
import cz.cvut.fel.ear.sis.model.Program;
import cz.cvut.fel.ear.sis.model.Attendance;
import jakarta.persistence.TypedQuery;
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

    public Course findCourseByName(String name) {
        TypedQuery<Course> query = em.createNamedQuery("findCourseByName", Course.class);
        query.setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    public Course findCourseById(int id) {
        TypedQuery<Course> query = em.createNamedQuery("findCourseById", Course.class);
        query.setParameter("id", id);
        return query.getResultStream().findFirst().orElse(null);
    }
}
