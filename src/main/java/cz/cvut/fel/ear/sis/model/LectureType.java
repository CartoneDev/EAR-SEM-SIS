package cz.cvut.fel.ear.sis.model;

public enum LectureType {
    LECTURE("LECTURE"), PRACTISE("PRACTISE"), LABORATORY("LAB");

    private final String name;

    LectureType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
