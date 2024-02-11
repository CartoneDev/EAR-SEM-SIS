package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.EnrollmentRecord;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRecordDao extends BaseDao<EnrollmentRecord>{
    public EnrollmentRecordDao() {super(EnrollmentRecord.class); }

    public EnrollmentRecord findByStudentIdAndCourseId(int studentId, int courseId) {
        return em.createQuery("SELECT e FROM EnrollmentRecord e WHERE e.student.id = :studentId AND e.course.id = :courseId", EnrollmentRecord.class)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .getSingleResult();
    }

}
