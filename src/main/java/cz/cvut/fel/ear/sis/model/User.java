package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@Entity
@Table(name = "sis_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
public class User extends AbstractEntity {
//    @Basic(optional = false)
//    @Column(name = "user_type", nullable = false)
//    private String userType;

    @Basic(optional = false)
    @Column(name = "email", nullable = false)
    @Setter
    private String email;
    @Basic(optional = false)
    @Column(name = "username", nullable = false, unique = true)
    @Setter
    private String username;
    @Basic(optional = false)
    @Column(name = "firstname", nullable = false)
    @Setter
    private String firstName;
    @Basic(optional = false)
    @Column(name = "lastname", nullable = false)
    @Setter
    private String lastName;
    @Basic(optional = false)
    @Column(name = "password", nullable = false)
    private String passwordFingerprint;
    public void setPassword(String password) {
        this.passwordFingerprint = password;
    }
    public User() {
//        this.username = "guest";
    }
    @Override
    public String toString() {
        return "User{" + firstName + " " + lastName + " (" + username + ")}";
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(passwordFingerprint));
    }

}
