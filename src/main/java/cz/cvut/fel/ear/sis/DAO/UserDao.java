package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User>{
    public UserDao() {super(User.class); }
}
