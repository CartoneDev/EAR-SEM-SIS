package cz.cvut.fel.ear.sis.model;

public enum RoomType {
    AUDITORIUM("AUDIT"), CLASSROOM("CLASS"), PC_ROOM("PC_ROOM");

    private final String name;

    RoomType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
