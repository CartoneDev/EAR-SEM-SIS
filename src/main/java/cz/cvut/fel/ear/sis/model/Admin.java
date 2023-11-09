package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("ADMIN")
public class Admin extends User{

}
