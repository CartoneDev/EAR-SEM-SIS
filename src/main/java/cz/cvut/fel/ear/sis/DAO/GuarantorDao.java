package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Course;
import cz.cvut.fel.ear.sis.model.Guarantor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GuarantorDao extends BaseDao<Guarantor>{
    public GuarantorDao() {super(Guarantor.class); }

    public List<Guarantor> findCourseGuarantors(Course course) {
        return em.createNamedQuery("findCourseGuarantors", Guarantor.class)
                .setParameter("course", course)
                .getResultList();
    }
}
