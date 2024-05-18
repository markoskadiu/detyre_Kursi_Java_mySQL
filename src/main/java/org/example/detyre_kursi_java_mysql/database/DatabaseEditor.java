package org.example.detyre_kursi_java_mysql.database;

// MY CLASSES AND ENUMS

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.example.detyre_kursi_java_mysql.enums.StudentFields;
import org.example.detyre_kursi_java_mysql.enums.TeacherField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseEditor {

    // DATABASE ATTRIBUTES
    private static final Logger logger = Logger.getLogger(LoadFromDatabase.class.getName());

    private static final String URL = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11703584";
    private static final String USER = "sql11703584";
    private static final String PASSWORD = "qph9ucVZv6";

    private static final Alert alert = new Alert(Alert.AlertType.ERROR);

    //CONSTRUCTOR
    public DatabaseEditor(StudentFields fields, String value)
    {
        String editVar = "";
        switch (fields){
            case EMAIL -> editVar = "email";
            case GENDER -> editVar = "gender";
            case ADDRESS -> editVar = "address";
            case COUNTRY -> editVar = "country";
            case FIRST_NAME -> editVar = "first_name";
            case LAST_NAME -> editVar = "last_name";
            case PHONE_NUMBER -> editVar = "phone_number";
            case DATE_OF_BIRTH -> editVar = "date_of_birth";
            case ENROLLMENT_DATE -> editVar = "registration_date";
            case STUDENT_PASSWORD -> editVar = "student_password";
            default -> System.err.println("No data selected to change");
        }
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            logger.info("Connected to database \n");


            String sql = new StringBuilder().append("UPDATE Students SET ").append(editVar).append(" = ? WHERE " +
                    "student_id = ?").toString();

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


               preparedStatement.setString(1, value);
               preparedStatement.setString(2, LoadFromDatabase.getVar_StudentID());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    logger.info(editVar+ " Altered Succesfully \n");
                }
            }catch (SQLException e) {
                logger.severe("SQL error occurred: " + editVar + "Could NOT Be Altered ");
                alert.setTitle("Error");
                alert.setHeaderText("SQL Error");
                alert.setContentText("SQL error occurred: " + editVar + "Could NOT Be Altered " + e.getMessage());
            }
        } catch (SQLException e) {
            logger.severe("SQL error occurred: " + editVar + "Could NOT Be Altered ");
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("SQL error occurred: " + editVar + "Could NOT Be Altered " + e.getMessage());
        }
    }
    public DatabaseEditor(TeacherField fields, String value)
    {
        String editVar = switch (fields) {
            case EMAIL -> "email";
            case GENDER -> "gender";
            case ADDRESS -> "address";
            case COUNTRY -> "Country";
            case FIRST_NAME -> "first_name";
            case LAST_NAME -> "last_name";
            case PHONE_NUMBER -> "phone_number";
            case DATE_OF_BIRTH -> "date_of_birth";
            case TEACHERS_PASSWORD -> "teachers_password";
            case HIRE_DATE -> "registration_date";
            default -> {
                System.err.println("No data selected to change");
                yield "";
            }
        };

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            logger.info("Connected to database \n");

            String sql = "UPDATE Teachers SET " + editVar + " = ? WHERE teacher_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, value);
                preparedStatement.setString(2, String.valueOf(LoadFromDatabase.getTeacherID()));

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    logger.info(editVar + " Altered Successfully \n");

                }
            } catch (SQLException e) {
                logger.severe("SQL error occurred: " + editVar + "Could NOT Be Altered ");
                alert.setTitle("Error");
                alert.setHeaderText("SQL Error");
                alert.setContentText("SQL error occurred: " + editVar + "Could NOT Be Altered " + e.getMessage());
            }
        } catch (SQLException e) {
            logger.severe("SQL error occurred while connection");
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("SQL error occurred while connection\n " + e.getMessage());

        }
    }

    public static void addStudentGrade(int year,int credits,int seminar,int midterm,int project,int exam,
                                int studentID,int courseID)
    {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            logger.info("Connected to database \n");

            String sql = "insert into Grades (year, credits, seminar_points, midterm_points, project_points, " +
                    "final_exam_points,student_id, course_id) " +
                    "values (?,?,?,?,?,?,?,?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, year);
                preparedStatement.setInt(2, credits);
                preparedStatement.setInt(3, seminar);
                preparedStatement.setInt(4, midterm);
                preparedStatement.setInt(5, project);
                preparedStatement.setInt(6, exam);
                preparedStatement.setInt(7, studentID);
                preparedStatement.setInt(8, courseID);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    logger.info(" Grade Added Successfully \n");
                }
                updateFinalGrade(connection);
                updateCourseName(connection);
            } catch (SQLException e) {
                logger.severe("SQL error occurred:  Grade could not be added ");
                alert.setTitle("Error");
                alert.setHeaderText("SQL Error");
                alert.setContentText("Grade could not be added\n " + e.getMessage());

            }
        } catch (SQLException e) {
            logger.severe("Could not connect to DB\n");
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("SQL error occurred while connection\n " + e.getMessage());

        }
    }

    private static void updateFinalGrade(Connection connection)
    {
        String sql = "call UpdateFinalGrade();";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Execute the stored procedure
            preparedStatement.execute();
            logger.info("Final grades updated successfully\n");
        } catch (SQLException e) {
            logger.severe("SQL error occurred: " + e.getMessage());
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("Could not update final Grade\n " + e.getMessage());
        }
    }

    private static void updateCourseName(Connection connection)
    {
        String sql = "call UpdateGradesCourseNames();";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Execute the stored procedure
            preparedStatement.execute();

        } catch (SQLException e) {
            logger.severe("SQL error occurred: " + e.getMessage());
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("Could not update course name.\n " + e.getMessage());
        }
    }

    public static void registerAStudent(Button butt,String name, String surname, String dob, String gender,
                                        String address,
                                        String courntry, String email, String password, String number, String dor,
                                        String program){

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            logger.info("Connected");

            String sql = "insert into Students (first_name, last_name, date_of_birth, gender, address, country, " +
                    "email, student_password, phone_number, university_id, registration_date, program_id) values " +
                    "(?,?,?,?,?,?,?,?,?,1,?,?);";

            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1,name);
                statement.setString(2,surname);
                statement.setString(3,dob);
                statement.setString(4,gender);
                statement.setString(5,address);
                statement.setString(6,courntry);
                statement.setString(7,email);
                statement.setString(8,password);
                statement.setString(9,number);
                statement.setString(10,dor);
                statement.setString(11,program);

                statement.execute();

                logger.info("Registered Student Successfully \n");
                butt.setStyle("-fx-background-color: green; -fx-text-fill: white;");



            }

        } catch (SQLException e) {
            logger.severe("Failed!");
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText("Could NOT Register Student\n " + e.getMessage());
            butt.setStyle("-fx-background-color: red; -fx-text-fill: white;");



        }


    }


}

