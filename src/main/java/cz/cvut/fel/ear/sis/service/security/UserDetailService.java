package cz.cvut.fel.ear.sis.service.security;

import cz.cvut.fel.ear.sis.DAO.UserDao;
import cz.cvut.fel.ear.sis.security.model.SISUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import cz.cvut.fel.ear.sis.model.User;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SISUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        return new SISUserDetails(user);
    }

    public void verifyPassword(UserDetails userDetails, String password) throws AuthenticationException{
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new AuthenticationException("Invalid password") {
            };
        }
    }
}
