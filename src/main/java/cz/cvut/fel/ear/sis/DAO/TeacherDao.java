package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDao extends BaseDao<Teacher>{
    public TeacherDao() {super(Teacher.class); }

    public Teacher findTeacherByName(String name) {
        return em.createQuery("SELECT t FROM Teacher t WHERE t.firstName = :name", Teacher.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
