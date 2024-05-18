package org.example.detyre_kursi_java_mysql.student_op;

public class StudentCourses {
    private final int COUNT;
    private final String COURSEID;
    private final String COURSENAME;

    public StudentCourses(int count, String courseId, String courseName)
    {
        COUNT = count;
        COURSEID = courseId;
        COURSENAME = courseName;
    }

    public String getCOURSEID()
    {
        return COURSEID;
    }

    public String getCOURSENAME()
    {
        return COURSENAME;
    }

    public int getCOUNT()
    {
        return COUNT;
    }

}
