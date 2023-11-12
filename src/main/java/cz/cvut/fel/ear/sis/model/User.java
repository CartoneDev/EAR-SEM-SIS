package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "SIS_USER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User extends AbstractEntity{
    @Basic(optional = false)
    @Column(name = "user_type", nullable = false)
    @Setter
    private String userType;

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
    public void setPassword(String password, PasswordEncoder encoder) {
        this.passwordFingerprint = encoder.encode(password);
    }
    public User() {
//        this.username = "guest";
    }
    @Override
    public String toString() {
        return "User{" + firstName + " " + lastName + " (" + username + ")}";
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        setPassword(passwordFingerprint, passwordEncoder);
    }
}
