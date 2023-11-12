package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao extends BaseDao<Student>{
    public StudentDao() {super(Student.class); }

}
