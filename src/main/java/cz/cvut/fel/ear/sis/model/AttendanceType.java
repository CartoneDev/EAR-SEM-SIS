package cz.cvut.fel.ear.sis.model;

public enum AttendanceType {
    MANDATORY("MANDATORY"), VOLUNTARY("VOLUNTARY"), VOLUNTARY_FORCED("VOLUNTARY_FORCED");
    AttendanceType(String name) {
        this.name = name;
    }
    private final String name;
    @Override
    public String toString() {
        return name;
    }
}
