package cz.cvut.fel.ear.sis.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GARANT")
public class Guarantor extends Teacher{
}
