package org.example.detyre_kursi_java_mysql.student_op;

public  class StudentGrades {
    private final String year;
    private final String courseName;
    private final double credits;
    private final double seminarPoints;
    private final double midtermPoints;
    private final double projectPoints;
    private final double finalExamPoints;
    private final double finalGrade;

    public StudentGrades(String year, String courseName, double credits, double seminarPoints,
                         double midtermPoints, double projectPoints, double finalExamPoints, double finalGrade) {
        this.year = year;
        this.courseName = courseName;
        this.credits = credits;
        this.seminarPoints = seminarPoints;
        this.midtermPoints = midtermPoints;
        this.projectPoints = projectPoints;
        this.finalExamPoints = finalExamPoints;
        this.finalGrade = finalGrade;
    }

    // Getters for each property
    public String getYear() {
        return year;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getCredits() {
        return credits;
    }

    public double getSeminarPoints() {
        return seminarPoints;
    }

    public double getMidtermPoints() {
        return midtermPoints;
    }

    public double getProjectPoints() {
        return projectPoints;
    }

    public double getFinalExamPoints() {
        return finalExamPoints;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    // Implement the getStudentID method
    private static String getStudentID() {
        // Implement this method to return the student ID
        return "YourStudentID";
    }
}
