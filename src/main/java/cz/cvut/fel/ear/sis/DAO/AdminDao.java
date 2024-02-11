package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Admin;
import org.hibernate.annotations.Persister;
import org.springframework.stereotype.Repository;

@Repository
//@Persister(impl = SubUserPersister.class)
public class AdminDao extends BaseDao<Admin>{
    public AdminDao() {super(Admin.class); }
    @Override
    public void persist(Admin entity) {
        super.persist(entity);
    }
}
