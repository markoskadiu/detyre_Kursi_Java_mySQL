package org.example.detyre_kursi_java_mysql;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.detyre_kursi_java_mysql.database.DatabaseEditor;
import org.example.detyre_kursi_java_mysql.student_op.StudentCourses;
import org.example.detyre_kursi_java_mysql.student_op.StudentGrades;
import org.example.detyre_kursi_java_mysql.database.InvalidCredentialsException;
import org.example.detyre_kursi_java_mysql.database.LoadFromDatabase;
import org.example.detyre_kursi_java_mysql.enums.Country;
import org.example.detyre_kursi_java_mysql.enums.Gender;
import org.example.detyre_kursi_java_mysql.enums.StudentFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class StudentManagement implements Initializable {


    public AnchorPane studentPane;
    //      First Tab Courses
    @FXML private TableView<StudentCourses> courseTable;
    @FXML private TableColumn<StudentCourses , Integer> courseCountColumn;
    @FXML private TableColumn<StudentCourses , String> courseIDCoulmn;
    @FXML private TableColumn<StudentCourses, String> courseNameColumn;
    @FXML private ToggleGroup courseToggleGroup;
    @FXML private ToggleButton thirdYearToggle;
    @FXML private ToggleButton secondYearToggle;
    @FXML private ToggleButton firstYearToggle;
    @FXML private ToggleButton allTimeToggle;

    // Second Tab GRADES TAB
    @FXML private Tab gradesTab;
    @FXML private TableView<StudentGrades> gradesTable;
    @FXML private TableColumn<StudentGrades, String> courseColumn;
    @FXML private TableColumn<StudentGrades, String> yearColumn;
    @FXML private TableColumn<StudentGrades, Double> creditsColumn;
    @FXML private TableColumn<StudentGrades, Double> seminarColumn;
    @FXML private TableColumn<StudentGrades, Double> midtermColumn;
    @FXML private TableColumn<StudentGrades, Double> projectColumn;
    @FXML private TableColumn<StudentGrades, Double> examColumn;
    @FXML private TableColumn<StudentGrades, Double> gradeColumn;
    @FXML private Label averageGradeLabel;
    @FXML private ToggleButton thirdYearToggle1;
    public ToggleGroup gradeToggleGroup;
    @FXML private ToggleButton secondYearToggle1;
    @FXML private ToggleButton firstYearToggle1;
    @FXML private ToggleButton allTimeToggle1;

    // THIRD TAB DETAILS
    @FXML private Label newDataLabel;
    @FXML private ChoiceBox<StudentFields> elementChooser;
    @FXML private TextField loadDataField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Country> countryChoiceBox;
    @FXML private ChoiceBox<Gender> genderChoiceBox;
    @FXML private DatePicker loadDatePicker;
    @FXML private Button loadNewDataButton;
    @FXML private TextArea textArea;
    @FXML private Button alterbutton;

    // Stage and Scene variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String elementGottenFromLoadField;
    @FXML private Label nameLabel;

//    INITIALISATION
    public  void showNotification(AnchorPane root, String message)
    {
        /* Create a label for the notification message*//**/
        Label notificationLabel = new Label(message);
        notificationLabel.setStyle("-fx-background-color: #3c4572; -fx-padding: 10px; -fx-text-fill: white; " +
                "-fx-border-color: #515c93; -fx-border-radius: 6px; -fx-font-size: 17;-fx-text-alignment: center");
        notificationLabel.setTextAlignment(TextAlignment.CENTER);
        notificationLabel.setAlignment(Pos.CENTER);
        notificationLabel.setMinHeight(75);
        notificationLabel.setMaxHeight(80);
        notificationLabel.setMinWidth(220);
        notificationLabel.setMaxWidth(225);



        // Create a stack pane to hold the notification label
        StackPane notificationPane = new StackPane(notificationLabel);
        notificationPane.setLayoutX(600);
        notificationPane.setLayoutY(460);
        notificationPane.setAlignment(Pos.CENTER);
        notificationLabel.setWrapText(true);

        // Create a timeline to animate the notification
        Timeline timeline = new Timeline();
        String soundPath = LoginController.class.getResource("audio/error.wav").toString();
        AudioClip audioClip = new AudioClip(soundPath);
        audioClip.play();
    //        audioClip.pl
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(notificationPane.opacityProperty(),0.0)),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(notificationPane.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(notificationPane.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3.5),
                        new KeyValue(notificationPane.opacityProperty(), 0.0))
        );

        // Add the notification pane to the root stack pane
        root.getChildren().add(notificationPane);

        // Show the notification by playing the timeline
        timeline.play();
    }
    public void displayData(String data, String name)
    {
        nameLabel.setText("Hello " + name);
        textArea.setText(data);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //        Courses Tab
        allTimeToggle.fire();

        //         StudentGrades Tab
        initialiseGradesColum();

        //        Details Tab
        elementChooser.getItems().addAll(StudentFields.values());
        countryChoiceBox.getItems().addAll(Country.values());
        genderChoiceBox.getItems().addAll(Gender.values());
        elementChooser.setOnAction(this::setFieldType);

    }

    public void logOut(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        root = loader.load();

        // Get controller of StudentManagement
        @SuppressWarnings("unused") LoginController management = loader.getController();

        // Get the current stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new Scene with loaded root
        scene = new Scene(root);

        // Set the new Scene to the stage
        stage.setScene(scene);
        stage.show();


    }

//    First Tab Courses
    
    @FXML
    private void loadCourses(ActionEvent actionEvent) throws RuntimeException
    {
        setCourseTableValues();

    if (actionEvent.getSource() == firstYearToggle)
        initialiseCourseColums("1");
    else if (actionEvent.getSource() == secondYearToggle)
        initialiseCourseColums("2");
    else if (actionEvent.getSource() == thirdYearToggle)
        initialiseCourseColums("3");
    else if (actionEvent.getSource() == allTimeToggle)
        initialiseCourseColums();

}

    private void setCourseTableValues(){
        courseCountColumn.setCellValueFactory(new PropertyValueFactory<>("COUNT"));
        courseIDCoulmn.setCellValueFactory(new PropertyValueFactory<>("COURSEID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("COURSENAME"));
    }

    private void initialiseCourseColums(){
        LoadFromDatabase.getStudentCoursesFromDB(courseTable);
    }
    private void initialiseCourseColums(String year)
    {
        LoadFromDatabase.getStudentCoursesFromDB(year,courseTable);
    }

//    SECOND TAB GRADES
    private void setColumValuesTye()
    {
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        seminarColumn.setCellValueFactory(new PropertyValueFactory<>("seminarPoints"));
        midtermColumn.setCellValueFactory(new PropertyValueFactory<>("midtermPoints"));
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("projectPoints"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("finalExamPoints"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
    }
    private void initialiseGradesColum()
    {
        setColumValuesTye();
        getGradesRows();
    }
    private void getGradesRows(int year)
    {
        LoadFromDatabase.getAllStudentGradesByCourse(year,gradesTable);
        LoadFromDatabase.calculateAverageGrade(year,averageGradeLabel);
    }
    private void getGradesRows()
    {
        LoadFromDatabase.getAllStudentGradesByCourse(gradesTable);
        LoadFromDatabase.calculateAverageGrade(averageGradeLabel);
    }

    @FXML
    private void getAvg(ActionEvent actionEvent)
    {
        if (actionEvent.getSource() == firstYearToggle1)
        {
            getGradesRows(1);
        } else if (actionEvent.getSource() == secondYearToggle1) {
            getGradesRows(2);
        } else if (actionEvent.getSource() == thirdYearToggle1) {
            getGradesRows(3);
        } else if (actionEvent.getSource() == allTimeToggle1) {
            getGradesRows();
        }
    }


//    THIRD TAD DETAILS

    public void alterData(ActionEvent actionEvent)
    {

        alterbutton.setVisible(false);
        loadNewDataButton.setVisible(true);
        newDataLabel.setVisible(true);
        elementChooser.setVisible(true);

    }
    public void setFieldType(ActionEvent actionEvent)
    {

        newDataLabel.setLayoutY(165);

        switch (elementChooser.getValue())
        {
            case STUDENT_PASSWORD:
                passwordField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                loadDataField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Must contain at least one upper case letter");
                break;
            case COUNTRY:
                countryChoiceBox.setVisible(true);

                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDataField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a country");
                break;
            case GENDER:
                genderChoiceBox.setVisible(true);

                countryChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDataField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a gender");
                break;
            case FIRST_NAME:
                loadDataField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a first name");
                break;
            case LAST_NAME:
                loadDataField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a last name");
                break;
            case EMAIL:
                loadDataField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a email");
                loadDataField.setPromptText("email@example.com");
                break;
            case ADDRESS:
                loadDataField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a address");
                loadDataField.setPromptText("Street , City ZIP");
                break;
            case ENROLLMENT_DATE:
                loadDatePicker.setVisible(true);

                loadDataField.setVisible(false);
                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);

                newDataLabel.setText("Select an enrollment date");
                loadDataField.setPromptText("YYYY-MM-DD");
                break;
            case PHONE_NUMBER:
                loadDataField.setVisible(true);

                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                loadDatePicker.setVisible(false);

                newDataLabel.setText("Select a phone number");
                loadDataField.setPromptText("06...");
                break;
            case DATE_OF_BIRTH:
                loadDatePicker.setVisible(true);

                loadDataField.setVisible(false);
                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);

                newDataLabel.setText("Select a date of birth");
                loadDataField.setPromptText("YYYY-MM-DD");
                break;

            default:
                loadDataField.setVisible(false);
                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);
                break;
        }

    }
    public void loadNewData(ActionEvent actionEvent) throws InvalidCredentialsException
    {

        switch (elementChooser.getValue()){
            case STUDENT_PASSWORD:
                elementGottenFromLoadField = passwordField.getText();
                new DatabaseEditor(StudentFields.STUDENT_PASSWORD , elementGottenFromLoadField);
                break;
            case COUNTRY:
                elementGottenFromLoadField = countryChoiceBox.getValue().toString();
                new DatabaseEditor(StudentFields.COUNTRY , elementGottenFromLoadField);
                break;
            case GENDER:
                elementGottenFromLoadField = genderChoiceBox.getValue().toString();
                new DatabaseEditor(StudentFields.GENDER , elementGottenFromLoadField );
                break;
            case FIRST_NAME:
                elementGottenFromLoadField = loadDataField.getText();
                new DatabaseEditor(StudentFields.FIRST_NAME , elementGottenFromLoadField);
                break;
            case LAST_NAME:
                elementGottenFromLoadField = loadDataField.getText();
                new DatabaseEditor(StudentFields.LAST_NAME , elementGottenFromLoadField);
                break;
            case EMAIL:
                elementGottenFromLoadField = loadDataField.getText();
                new DatabaseEditor(StudentFields.EMAIL , elementGottenFromLoadField);
                break;
            case ADDRESS:
                elementGottenFromLoadField = loadDataField.getText();
                new DatabaseEditor(StudentFields.ADDRESS , elementGottenFromLoadField);
                break;
            case ENROLLMENT_DATE:

                try {
                    if (loadDatePicker.getValue() == null){
                        new Alert(Alert.AlertType.ERROR,"Check the field and try again").show();
                        break;
                    }
                    elementGottenFromLoadField = loadDatePicker.getValue().toString();

                    new DatabaseEditor(StudentFields.ENROLLMENT_DATE , elementGottenFromLoadField);
                } catch (DateTimeParseException e) {
                    new Alert(Alert.AlertType.ERROR,"Check the field and try again").show();
                }
                break;
            case PHONE_NUMBER:
                elementGottenFromLoadField = loadDataField.getText();
                new DatabaseEditor(StudentFields.PHONE_NUMBER, elementGottenFromLoadField);
                break;
            case DATE_OF_BIRTH:

                try {
                    elementGottenFromLoadField = loadDatePicker.getValue().toString();
                    new DatabaseEditor(StudentFields.DATE_OF_BIRTH , elementGottenFromLoadField );
                } catch (NullPointerException|DateTimeParseException e) {
                    new Alert(Alert.AlertType.ERROR,"Check the field and try again" + e.getCause()).show();
                }
                break;
            default:
                elementGottenFromLoadField = loadDataField.getText();
                System.out.println(elementGottenFromLoadField);
                break;
        }
        clearAllFields();

        LoadFromDatabase.getStudentDataFromDB(LoadFromDatabase.getVar_studentEmail(),LoadFromDatabase.getVar_studentPassword());
        displayData(LoadFromDatabase.getAllInfoStudent(),LoadFromDatabase.getVar_studentFirstName());
        showNotification(studentPane,"Data Updated;");

    }
    private void clearAllFields()
    {
        passwordField.clear();
        loadDataField.clear();
        loadDatePicker.setValue(null);
        countryChoiceBox.setValue(null);
        genderChoiceBox.setValue(null);
    }












}
