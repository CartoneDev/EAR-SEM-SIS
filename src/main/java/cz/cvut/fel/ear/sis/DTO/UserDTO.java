package cz.cvut.fel.ear.sis.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import cz.cvut.fel.ear.sis.model.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserDTO {

    private String role;
    private final String email;
    private final String username;
    private final String firstName;
    private final String lastName;
    public UserDTO(Guarantor guarantor) {
        this((User) guarantor);
        this.role = "Guarantor";
    }

    public UserDTO(Teacher teacher) {
        this((User) teacher);
        this.role = "Teacher";
    }

    public UserDTO(Admin admin) {
        this((User) admin);
        this.role = "Admin";
    }

    public UserDTO(Student student) {
        this((User) student);
        this.role = "Student";
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role =  "USER";
    }

    public Integer getId(User user) {
        return user.getId();
    }
}
