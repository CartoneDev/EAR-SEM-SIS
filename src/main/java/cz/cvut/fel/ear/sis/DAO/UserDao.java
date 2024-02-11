package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.User;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class UserDao extends BaseDao<User>{
    public UserDao() {super(User.class); }


    public User findByUsername(String username) {
        Query query = em.createNamedQuery("User.findByUsername", User.class);

        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    public String getUserType(String username) {
        User user = findByUsername(username);
        if (user == null) {
            return "NOPE";
        }
        return user.getClass().descriptorString();


    }
}
