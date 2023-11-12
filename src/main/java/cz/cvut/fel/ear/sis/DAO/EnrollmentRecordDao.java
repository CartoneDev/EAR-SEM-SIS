package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.EnrollmentRecord;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRecordDao extends BaseDao<EnrollmentRecord>{
    public EnrollmentRecordDao() {super(EnrollmentRecord.class); }
}
