package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User{

}
