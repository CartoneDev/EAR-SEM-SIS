package cz.cvut.fel.ear.sis.DAO;

import cz.cvut.fel.ear.sis.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao extends BaseDao<Room>{
    public RoomDao() {super(Room.class); }
}
