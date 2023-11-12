package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends BaseDao<Admin>{
    public AdminDao() {super(Admin.class); }
}
