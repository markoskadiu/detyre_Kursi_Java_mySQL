package org.example.detyre_kursi_java_mysql.database;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import org.example.detyre_kursi_java_mysql.LoginController;
import org.example.detyre_kursi_java_mysql.admin.StudentList;
import org.example.detyre_kursi_java_mysql.admin.TeacherList;
import org.example.detyre_kursi_java_mysql.enums.Country;
import org.example.detyre_kursi_java_mysql.enums.Gender;
import org.example.detyre_kursi_java_mysql.student_op.StudentCourses;
import org.example.detyre_kursi_java_mysql.student_op.StudentGrades;
import org.example.detyre_kursi_java_mysql.teacher_op.StudentParticipation;
import org.example.detyre_kursi_java_mysql.teacher_op.TeacherCourse;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

@SuppressWarnings("ALL")
public class LoadFromDatabase {

//    DATABASE ATTRIBUTES
    private static final Logger logger = Logger.getLogger(LoadFromDatabase.class.getName());

    private static final String URL = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11703584";
    private static final String USER = "sql11703584";
    private static final String PASSWORD = "qph9ucVZv6";
//    DATABASE RELATED
    static Connection connection;
//    STUDENT ATTRIBUTES
    private static String var_studentEmail;
    private static String var_studentPassword;
    private static String var_studentFirstName;
    private static String var_studentLastName;
    private static Date var_studentDateOfBirth;
    private static String var_studentGender;
    private static String var_studentPhoneNumber;
    private static String var_studentAddress;
    private static String var_studentCountry;
    private static Date var_studentRegisrationData;
    private static String var_studentStudyProgram;
    private static Integer var_StudentID;
    private static String[] var_studentCourses;
    // Teacher attributes
    private static String var_teacherFirstName;
    private static String var_teacherLastName;
    private static Date var_teacherDateOfBirth;
    private static String var_teacherGender;
    private static String var_teacherAddress;
    private static String var_teacherCountry;
    private static String var_teacherEmail;
    private static String var_teacherPassword;
    private static String var_teacherPhoneNumber;
    private static int var_teacherID;
    private static Vector<String> var_teacherCourses;
    private static List<String> var_studyPrograms;

    private static Alert alert = new Alert(Alert.AlertType.ERROR);

//  ESTABLISHING CONECTION TO THE DATABASE
    public static void connectToDatabase()
    {

        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            logger.info("Connected to database");


        } catch (SQLException e) {
            logger.severe("Failed to connect to database");
            e.printStackTrace();

            alert.setHeaderText("Failed to connect to database");
            alert.setContentText("Database connection error : " + e.getErrorCode());
            alert.showAndWait();
        }

    }

//    STUDENT METHODS
    public static void getStudentDataFromDB(String parameterUserEmail, String parameterUserpassword) throws InvalidCredentialsException
    {

            String sql = "SELECT * FROM Students S Join StudyProgram P on S.program_id = P.program_id WHERE email = ? AND student_password = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, parameterUserEmail);
                statement.setString(2, parameterUserpassword);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Populate student information if credentials are valid
                        var_studentFirstName = resultSet.getString("first_name");
                        var_studentLastName = resultSet.getString("last_name");
                        var_StudentID = resultSet.getInt("student_id");
                        var_studentDateOfBirth = resultSet.getDate("date_of_birth");
                        var_studentGender = resultSet.getString("gender");
                        var_studentEmail = resultSet.getString("email");
                        var_studentPhoneNumber = resultSet.getString("phone_number");
                        var_studentAddress = resultSet.getString("address");
                        var_studentCountry = resultSet.getString("country");
                        var_studentRegisrationData = resultSet.getDate("registration_date");
                        var_studentStudyProgram = resultSet.getString("program_name");
                        var_studentPassword = resultSet.getString("student_password");
                    } else {
                        throw new InvalidCredentialsException("");
                    }
                }
            } catch (SQLException e) {
                logger.severe("SQL error occurred: " + e.getMessage());
                throw new InvalidCredentialsException("SQL error occurred: " + e.getMessage());
            }
    }
    public static void getStudentCoursesFromDB(String year,TableView<StudentCourses> tableView)
    {
        ObservableList<StudentCourses> coursesList = FXCollections.observableArrayList();
            String sql = "SELECT course_id AS COURSEID, course_name AS COURSENAME" +
                    " FROM Courses" +
                    " JOIN Students ON Courses.program_id = Students.program_id" +
                    " WHERE Students.student_id = ?" +
                    " AND year = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, getVar_StudentID());
                statement.setString(2, year);

                try (ResultSet resultSet = statement.executeQuery()) {
                    int count = 1;
                    while (resultSet.next()) {
                        String courseId = resultSet.getString("COURSEID");
                        String courseName = resultSet.getString("COURSENAME");

                        StudentCourses studentCourses = new StudentCourses(count, courseId, courseName);

                        coursesList.add(studentCourses);
                        count++;
                    }
                }
                // Clear existing items in the table view and set new items
                tableView.getItems().clear();
                tableView.setItems(coursesList);
            } catch (SQLException e) {
            // Log and report SQL errors
            logger.severe("SQL error occurred: " + e.getMessage());
                Alert alert =  new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText("error occurred: " + e.getErrorCode());
                alert.show();
        }
    }
    public static void getStudentCoursesFromDB(TableView<StudentCourses> tableView)
    {

        ObservableList<StudentCourses> coursesList = FXCollections.observableArrayList();

        String sql = "SELECT course_id AS COURSEID, course_name AS COURSENAME" +
                " FROM Courses" +
                " JOIN Students ON Courses.program_id = Students.program_id" +
                " WHERE Students.student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, getVar_StudentID());

            try (ResultSet resultSet = statement.executeQuery()) {
                int count = 1;
                while (resultSet.next()) {
                    String courseId = resultSet.getString("COURSEID");
                    String courseName = resultSet.getString("COURSENAME");

                    StudentCourses studentCourses = new StudentCourses(count, courseId, courseName);

                    coursesList.add(studentCourses);
                    count++;
                }
            }
            // Clear existing items in the table view and set new items
            tableView.getItems().clear();
            tableView.setItems(coursesList);
        } catch (SQLException e) {
            // Log and report SQL errors
            logger.severe("SQL error occurred: " + e.getMessage());
            // Provide additional feedback to the UI if applicable

            alert.setHeaderText("SQL error occurred: ");
            alert.setContentText("Could not Load Courses" + e.getMessage());
            alert.show();
        }
    }

    public static void getAllStudentGradesByCourse(TableView<StudentGrades> tableView)
    {
        ObservableList<StudentGrades> studentGradesList = FXCollections.observableArrayList();


            String sql = "SELECT year, course_name, credits, seminar_points, midterm_points, project_points, " +
                    "final_exam_points, final_grade FROM Grades WHERE student_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql))
            {
                statement.setString(1, getVar_StudentID());

                try (ResultSet resultSet = statement.executeQuery())
                {
                    while (resultSet.next()) {
                        String year = resultSet.getString("year");
                        String courseName = resultSet.getString("course_name");
                        double credits = resultSet.getDouble("credits");
                        double seminarPoints = resultSet.getDouble("seminar_points");
                        double midtermPoints = resultSet.getDouble("midterm_points");
                        double projectPoints = resultSet.getDouble("project_points");
                        double finalExamPoints = resultSet.getDouble("final_exam_points");
                        double finalGrade = resultSet.getDouble("final_grade");

                        StudentGrades grade = new StudentGrades(year, courseName, credits, seminarPoints,
                                midtermPoints, projectPoints, finalExamPoints, finalGrade);

                        studentGradesList.add(grade);
                    }
                }

                // Clear existing items in the table view and set new items
                tableView.getItems().clear();
                tableView.setItems(studentGradesList);
            } catch (SQLException e) {
            // Log and report SQL errors
            logger.severe("SQL error occurred: " + e.getMessage());

            alert.setHeaderText("SQL error occurred: ");
            alert.setContentText("Could not Load StudentGrades" + e.getMessage());
            alert.show();
        }
    }
     public static void getAllStudentGradesByCourse(int studyYear, TableView<StudentGrades> tableView)
    {

        ObservableList<StudentGrades> studentGradesList = FXCollections.observableArrayList();

            String sql = "SELECT year, course_name, credits, seminar_points, midterm_points, project_points, " +
                    "final_exam_points, final_grade FROM Grades WHERE student_id = ? AND year = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql))
            {
                statement.setString(1, getVar_StudentID());
                statement.setInt(2, studyYear);

                try (ResultSet resultSet = statement.executeQuery())
                {
                    while (resultSet.next()) {
                        String year = resultSet.getString("year");
                        String courseName = resultSet.getString("course_name");
                        double credits = resultSet.getDouble("credits");
                        double seminarPoints = resultSet.getDouble("seminar_points");
                        double midtermPoints = resultSet.getDouble("midterm_points");
                        double projectPoints = resultSet.getDouble("project_points");
                        double finalExamPoints = resultSet.getDouble("final_exam_points");
                        double finalGrade = resultSet.getDouble("final_grade");

                        StudentGrades grade = new StudentGrades(year, courseName, credits, seminarPoints,
                                midtermPoints, projectPoints, finalExamPoints, finalGrade);

                        studentGradesList.add(grade);
                    }
                }

                // Clear existing items in the table view and set new items
                tableView.getItems().clear();
                tableView.setItems(studentGradesList);
            } catch (SQLException e) {
            // Log and report SQL errors
            logger.severe("SQL error occurred: " + e.getMessage());

                alert.setHeaderText("SQL error occurred: ");
                alert.setContentText("Could not Load StudentGrades" + e.getMessage());
                alert.show();
        }
    }
    public static void calculateAverageGrade(int year,Label label)
    {


        String sql = "select CalculateAverageWeightedGradeByYear(?,?) AS avg_grade";

            try (PreparedStatement statement = connection.prepareStatement(sql))
            {
                statement.setString(1, getVar_StudentID());
                statement.setString(2, String.valueOf(year));

                try (ResultSet resultSet = statement.executeQuery())
                {
                    if (resultSet.next())
                    {
                        double avgGrade = resultSet.getDouble("avg_grade");
                        label.setText("Average weighted grade for Y." + year +": " + avgGrade+" ");
                    } else
                    {

                    }
                }
            }catch (SQLException e) {
            // Log and report SQL errors
            logger.severe("SQL error occurred: " + e.getMessage());
            alert.setHeaderText("Could not calculate Average Grade");
            alert.setContentText("SQL error occurred: " + e.getMessage());
            alert.show();
        }
    }
    public static void calculateAverageGrade(Label label)
    {
        String sql = "SELECT CalculateAverageWeightedGrade(?) AS avg_grade";

        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, getVar_StudentID());

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    double avgGrade = resultSet.getDouble("avg_grade");
                    label.setText("Average weighted grade: " + avgGrade + " ");
                } else
                {
                    alert.setHeaderText("Could not calculate Average Grade");
                    alert.setContentText("NO DATA");
                    alert.show();
                }
            }
        }catch (SQLException e) {
        // Log and report SQL errors
        logger.severe("SQL error occurred: " + e.getMessage());
            alert.setHeaderText("Could not calculate Average Grade");
            alert.setContentText("SQL error occurred: " + e.getMessage());
            alert.show();
        }
    }


//    TEACHER METHODS
    public static void getTeacherDataFromDB(String parameterUserEmail, String parameterUserpassword) throws InvalidCredentialsException
    {

        String sql = "SELECT * FROM Teachers WHERE email = ? AND teachers_password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parameterUserEmail);
            statement.setString(2, parameterUserpassword);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var_teacherFirstName = resultSet.getString("first_name");
                    var_teacherLastName = resultSet.getString("last_name");
                    var_teacherDateOfBirth = resultSet.getDate("date_of_birth");
                    var_teacherGender = resultSet.getString("gender");
                    var_teacherAddress = resultSet.getString("address");
                    var_teacherCountry = resultSet.getString("Country");
                    var_teacherEmail = resultSet.getString("email");
                    var_teacherPassword = resultSet.getString("teachers_password");
                    var_teacherPhoneNumber = resultSet.getString("phone_number");
                    var_teacherID = resultSet.getInt("teacher_id");

                } else {

                    throw new InvalidCredentialsException("Invalid credentials");
                }
            }
        } catch (SQLException e) {
        // Log and report SQL errors
        logger.severe("Could not connect to database");

            alert.setHeaderText("Error connectiong: " + e.getErrorCode());
            alert.show();
    }
}
    public static void getCoursesForTeacher(TableView<TeacherCourse> tableView )
    {
        ObservableList<TeacherCourse> coursesList = FXCollections.observableArrayList();

        String sql = "SELECT T.department_id DID ,C.course_id CID ,C.course_name CUN " +
                "FROM Teachers T JOIN StudyProgram P ON P.dep_id = T.department_id " +
                "JOIN Courses C ON P.program_id = C.program_id " +
                "WHERE T.teacher_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, getTeacherID());
            try (ResultSet resultSet = statement.executeQuery()) {


                int number = 1;
                while (resultSet.next()) {
                    String DID = resultSet.getString("DID");
                    String CID = resultSet.getString("CID");
                    String CUN = resultSet.getString("CUN");

                    TeacherCourse course = new TeacherCourse(number,Integer.parseInt(DID), Integer.parseInt(CID),CUN);
                    coursesList.add(course);
                    number++;
                }
                tableView.getItems().clear();
                tableView.setItems(coursesList);
            }
        }catch (SQLException e) {
        alert.setHeaderText("Error connectiong: ");
        alert.setContentText("SQL error occurred: " + e.getMessage());
        alert.show();
        logger.severe("Could not connect to database: " + e.getMessage());
    }
}
    public static void getStudentsByCourse(TableView<StudentParticipation> tableView , int par_courseID)
    {
        ObservableList<StudentParticipation> studentParticipationList = FXCollections.observableArrayList();

        String sql = "SELECT C.course_id CID,C.course_name CUN,S.student_id SID, S.first_name NAME , S.last_name " +
                "SURNAME, P" +
                ".program_name " +
                "PROGRAM " +
                "from Students S " +
                "Join Courses C on C.program_id = S.program_id " +
                "Join StudyProgram P on C.program_id = P.program_id " +
                "where course_id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, par_courseID);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String CID = resultSet.getString("CID");
                    String CUN = resultSet.getString("CUN");
                    String SID = resultSet.getString("SID");
                    String NAME = resultSet.getString("NAME");
                    String SURNAME = resultSet.getString("SURNAME");
                    String PROGRAM = resultSet.getString("PROGRAM");

                    StudentParticipation participation = new StudentParticipation(CID,CUN,SID,SURNAME,NAME,PROGRAM);
                    studentParticipationList.add(participation);
                }
                tableView.getItems().clear();
                tableView.setItems(studentParticipationList);
            }
        }catch (SQLException e) {
            alert.setHeaderText("Error connectiong: ");
            alert.setContentText("Could not load Students For this couse\n: " + e.getMessage());
            alert.show();
            logger.severe("Could not load Students:  " + e.getMessage());
        }
    }

//    ADMIN METHODS

    public static void getAllStudyPrograms(List<String> par_studyPrograms) {
        // SQL query to select program names from the StudyProgram table
        String sql = "SELECT program_name AS name FROM StudyProgram;";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add each program name to the list
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                par_studyPrograms.add(name);
            }
        } catch (SQLException e) {
            logger.severe("Error occurred while retrieving study programs: " + e.getMessage());
            alert.setHeaderText("Error SQL: ");
            alert.setContentText("Error occurred while retrieving study programs: " + e.getMessage());

        }
    }
    public static void getAllStudents(TableView<StudentList> tableView){

        ObservableList<StudentList> studentLists = FXCollections.observableArrayList();

        String sql = "SELECT S.student_id SID, S.first_name  Name,S.last_name Surname,S.date_of_birth as Birthdate,S.gender Gender, S.address as Address," +
                "       S.country as Country, S.email as Email, S.phone_number as Phone," +
                "       S.registration_date Registration, P.program_name as Program," +
                "       S.student_password as Password " +
                "From Students S" +
                "    JOIN StudyProgram P on S.program_id = P.program_id " +
                "ORDER BY SID;";

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            PreparedStatement statement = connection.prepareStatement(sql);
            try(ResultSet resultSet = statement.executeQuery()){
                int i = 1;
                while (resultSet.next()){
                    int SID = resultSet.getInt("SID");
                    String Name = resultSet.getString("Name");
                    String Surname = resultSet.getString("Surname");
                    Date Birthdate = resultSet.getDate("Birthdate");
                    String Gender = resultSet.getString("Gender");
                    String Address = resultSet.getString("Address");
                    String Country = resultSet.getString("Country");
                    String Email = resultSet.getString("Email");
                    String Phone = resultSet.getString("Phone");
                    Date Registration = resultSet.getDate("Registration");
                    String Program = resultSet.getString("Program");
                    String Password = resultSet.getString("Password");

                    StudentList student = new StudentList(SID,Name,Surname,Birthdate,Gender,Address,Country,Email,Phone,
                            Registration,Program,Password);
                    studentLists.add(student);
                    i++;

                }
                System.out.println(i);
            }
            tableView.getItems().clear();
            tableView.setItems(studentLists);

        }catch (SQLException e) {
            logger.severe("Error occurred while retrieving the Students: " + e.getMessage());
            alert.setHeaderText("SQL Error!");
            alert.setContentText("Error occurred while retrieving the Students: " + e.getMessage());
            alert.show();

        }
    }
    public static void getAllTeachers(TableView<TeacherList> tableView)
    {

        ObservableList<TeacherList> teacherLists = FXCollections.observableArrayList();

        String sql = "select T.teacher_id TID, D.department_name DPN, T.first_name TFN, T.last_name TLN, T.date_of_birth TBD," +
                " T.registration_date TRD, T.gender TG, T.address TADD, T.Country TC, T.email TEM, T.phone_number TN, T.teachers_password TP " +
                "from Teachers T " +
                "Join Departments D on T.department_id = D.department_id " +
                "ORDER BY TID;";

            try{
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                int i = 1;
                while (resultSet.next()){

                    TeacherList teacher = new TeacherList(  resultSet.getInt("TID"),
                                                            resultSet.getString("DPN"),
                                                            resultSet.getString("TFN"),
                                                            resultSet.getString("TLN"),
                                                            resultSet.getDate("TBD"),
                                                            resultSet.getDate("TRD"),
                                                            Gender.valueOf(resultSet.getString("TG")),
                                                            resultSet.getString("TADD"),
                                                            Country.valueOf(resultSet.getString("TC")),
                                                            resultSet.getString("TEM"),
                                                            resultSet.getString("TN"),
                                                            resultSet.getString("TP")
                    );
                    teacherLists.add(teacher);
                    i++;

                }
                System.out.println(i);

//                tableView.getItems().clear();
                tableView.setItems(teacherLists);
            }catch (SQLException e) {
                logger.severe("Error occurred while retrieving the Teachers: " + e.getMessage());
                alert.setHeaderText("SQL Error!");
                alert.setContentText("Error occurred while retrieving the Teachers: " + e.getMessage());
                alert.show();

            }



    }



    //_________________________GETTER METHODS FOR STUDENT_________________________________________________\\

    public static String getVar_studentEmail()
    {
        return var_studentEmail;
    }
    public static String getVar_studentPassword()
    {
        return var_studentPassword;
    }
    public static String getVar_studentFirstName()
    {
        return var_studentFirstName;
    }
    public static String getVar_studentLastName()
    {
        return var_studentLastName;
    }
    public static Date getVar_studentDateOfBirth()
    {
        return var_studentDateOfBirth;
    }
    public static String getVar_studentGender()
    {
        return var_studentGender;
    }
    public static String getVar_studentPhoneNumber()
    {
        return var_studentPhoneNumber;
    }
    public static String getVar_studentAddress()
    {
        return var_studentAddress;
    }
    public static String getVar_studentCountry()
    {
        return var_studentCountry;
    }
    public static Date getVar_studentRegisrationData()
    {
        return var_studentRegisrationData;
    }
    public static String getVar_studentStudyProgram()
    {
        return var_studentStudyProgram;
    }
    public static String getVar_StudentID()
    {
        if (var_StudentID != null) {
            return var_StudentID.toString();
        }
        return "0";
    }
    public static String[] getVar_studentCourses() {
        return var_studentCourses;
    }
    public static String getAllInfoStudent()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder();
        // Append other information like Student ID, First Name, etc.
        // Define the width of each column
        // Define the width of each label and value
        int labelWidth = 15;
        int valueWidth = 10;
        String lines = "---------------------------------------------------\n";

// Append the data with proper spacing and formatting
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Student ID:", var_StudentID));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "First Name:", var_studentFirstName));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Last Name:", var_studentLastName));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Email:", var_studentEmail));

// Handle the date formatting
        try {
            stringBuilder.append(lines);
            stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Date of Birth:", dateFormat.format(var_studentDateOfBirth)));
        } catch (NullPointerException e) {
            stringBuilder.append(lines);
            stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Date of Birth:", "N/A"));
        }

        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n" ,
                "Study Program: " ,var_studentStudyProgram));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Gender:", var_studentGender));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Phone Number:", var_studentPhoneNumber));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Address:", var_studentAddress));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Country:", var_studentCountry));

// Handle the date formatting
        try {
            stringBuilder.append(lines);
            stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Enrollment Date:", dateFormat.format(var_studentRegisrationData)));
        } catch (NullPointerException e) {
            stringBuilder.append(lines);
            stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Enrollment Date:", "N/A"));
        }
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Password:", var_studentPassword));



        return stringBuilder.toString();
    }

// ======================== GETTER METHODS FOR TEACHERS =====================================================
    public static String getTeacherFirstName() {
        return var_teacherFirstName;
    }
    public static String getTeacherLastName() {
        return var_teacherLastName;
    }
    public static Date getTeacherDateOfBirth() {
        return var_teacherDateOfBirth;
    }
    public static String getTeacherGender() {
        return var_teacherGender;
    }
    public static String getTeacherAddress() {
        return var_teacherAddress;
    }
    public static String getTeacherCountry() {
        return var_teacherCountry;
    }
    public static String getTeacherEmail() {
        return var_teacherEmail;
    }
    public static String getTeacherPassword() {
        return var_teacherPassword;
    }
    public static String getTeacherPhoneNumber() {
        return var_teacherPhoneNumber;
    }
    public static int getTeacherID() {
        return var_teacherID;
    }

    public static String getAllInfoTeacher()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder();
        int labelWidth = 15;
        int valueWidth = 20;
        String lines = "---------------------------------------------------\n";

        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Teacher ID:", var_teacherID));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "First Name:", var_teacherFirstName));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Last Name:", var_teacherLastName));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Email:", var_teacherEmail));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Date of Birth:", var_teacherDateOfBirth != null ? dateFormat.format(var_teacherDateOfBirth) : "N/A"));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Gender:", var_teacherGender));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Phone Number:", var_teacherPhoneNumber));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Address:", var_teacherAddress));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Country:", var_teacherCountry));
        stringBuilder.append(lines);
        stringBuilder.append(String.format("%-" + labelWidth + "s | %-" + valueWidth + "s\n", "Password:", var_teacherPassword));
        stringBuilder.append(lines);

        return stringBuilder.toString();
    }

    public List<String> getVar_studyPrograms(){
        return var_studyPrograms;
    }






}
