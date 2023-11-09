package cz.cvut.fel.ear.sis.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


@Getter
@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room extends AbstractEntity{

    @Setter
    @Basic(optional = false)
    @Column(name = "identifier", nullable = false, unique = true)
    private String identifier;
    @Basic(optional = false)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Setter
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType type;

    public Room(String identifier, Integer capacity, RoomType type) {
        this.identifier = identifier;
        this.setCapacity(capacity);
        this.type = type;
    }

    public void setCapacity(Integer capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

}
