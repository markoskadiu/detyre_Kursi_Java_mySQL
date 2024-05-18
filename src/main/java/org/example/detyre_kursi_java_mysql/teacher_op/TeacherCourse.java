package org.example.detyre_kursi_java_mysql.teacher_op;


public class TeacherCourse {
    private final int DID;
    private final String CUN;
    private final int CID;
    private final int number;

    public TeacherCourse(int number, int DID, int CID, String CUN) {
        this.number = number;
        this.DID = DID;
        this.CID = CID;
        this.CUN = CUN;
    }

    // Rename the method to getDID()
    public int getDID() {
        return DID;
    }

    public int getCID() {
        return CID;
    }

    public String getCUN() {
        return CUN;
    }

    public int getNumber() {
        return number;
    }
}
