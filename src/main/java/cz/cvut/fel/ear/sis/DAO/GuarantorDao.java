package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Guarantor;
import org.springframework.stereotype.Repository;

@Repository
public class GuarantorDao extends BaseDao<Guarantor>{
    public GuarantorDao() {super(Guarantor.class); }
}
