package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Course;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDao extends BaseDao<Course> {
    public CourseDao() {super(Course.class); }

}
