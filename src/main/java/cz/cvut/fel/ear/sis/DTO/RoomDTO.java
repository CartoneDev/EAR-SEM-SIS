package cz.cvut.fel.ear.sis.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import cz.cvut.fel.ear.sis.model.Room;
import cz.cvut.fel.ear.sis.model.RoomType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Setter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomDTO extends Room {
    private String identifier;
    private Integer capacity;

    private RoomType type;
    public RoomDTO(Room room) {
        this.identifier = room.getIdentifier();
        this.capacity = room.getCapacity();
        this.type = room.getType();
    }
}
