package cz.cvut.fel.ear.sis.model;

public enum GradeType {
    UNGRADED("UNGRADED"), A("A"), B("B"), C("C"), D("D"), E("E"), F("F");
    GradeType(String grade) {
        this.grade = grade;
    }
    private final String grade;
    @Override
    public String toString() {
        return grade;
    }
}
