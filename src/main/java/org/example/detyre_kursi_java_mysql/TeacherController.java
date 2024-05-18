package org.example.detyre_kursi_java_mysql;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.detyre_kursi_java_mysql.database.DatabaseEditor;
import org.example.detyre_kursi_java_mysql.database.LoadFromDatabase;
import org.example.detyre_kursi_java_mysql.enums.Country;
import org.example.detyre_kursi_java_mysql.enums.Gender;
import org.example.detyre_kursi_java_mysql.enums.TeacherField;
import org.example.detyre_kursi_java_mysql.teacher_op.StudentParticipation;
import org.example.detyre_kursi_java_mysql.teacher_op.TeacherCourse;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class TeacherController implements Initializable {


    public AnchorPane teacherPane;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    public Label nameLabel;
    public TabPane teacherTabPane;

//    FIRST TAB

    @FXML private TableView<TeacherCourse> teacherCoursesTable;
    @FXML private TableColumn<TeacherCourse,Integer> nrCourseColumn;
    @FXML private TableColumn<TeacherCourse, Integer> depIDColumn;
    @FXML private TableColumn<TeacherCourse,Integer> courseIDColumn;
    @FXML private TableColumn<TeacherCourse,String> courseNameColumn;

//    SECOND TAB
    @FXML private Tab editTab;
    @FXML private TableView<StudentParticipation> studentsByCourseTable;
    @FXML private TableColumn<StudentParticipation , String> studentCourseNameColumn;
    @FXML private TableColumn<StudentParticipation , String> studentCourseIDColumn;
    @FXML private TableColumn<StudentParticipation , String> studentCourseStudentIDColumn;
    @FXML private TableColumn<StudentParticipation , String> studentNameColumn;
    @FXML private TableColumn<StudentParticipation , String> studentSurnameColumn;
    @FXML private TableColumn<StudentParticipation , String> studentCourseProgramColumn;

//    THIRD TAB
    @FXML private Spinner<Integer> yearSpinner ;
    @FXML private Spinner<Integer> creditsSpinner;
    @FXML private Spinner<Integer> seminarSpinner ;
    @FXML private Spinner<Integer> midtermSpinner ;
    @FXML private Spinner<Integer> projectSpinner ;
    @FXML private Spinner<Integer> examSpinner ;
    @FXML private Label studentAndCourseIDLabel;
    @FXML private Tab addGradesTab;
    @FXML private Button addGradeButton;

//    Fourth TAB
    @FXML private Tab detailsTab;
    @FXML private TextArea textArea;
    @FXML private Label newDataLabel;
    @FXML private ChoiceBox<TeacherField> elementChooser;
    @FXML private TextField loadDataField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Country> countryChoiceBox;
    @FXML private ChoiceBox<Gender> genderChoiceBox;
    @FXML private DatePicker loadDatePicker;
    @FXML private Button loadNewDataButton;
    @FXML private Button alterbutton;
//
    private String studentIDSelected;
    private int courseIDSelected;

    private String elementGottenFromLoadField;


    // OPERATIONS


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
                new KeyFrame(Duration.seconds(1.5),
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

    public void logOut(ActionEvent event) throws IOException
    {
       new StudentManagement().logOut(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Thread initialiseThread = new Thread( () -> {
            loadCourses();
            rowSelected();

        });
        initialiseThread.start();

        Thread second = new Thread( () -> {
            elementChooser.getItems().addAll(TeacherField.values());
            countryChoiceBox.getItems().addAll(Country.values());
            genderChoiceBox.getItems().addAll(Gender.values());
            elementChooser.setOnAction(this::setFieldType);
            setSpinnerValues();
            teacherTabPane.getTabs().remove(addGradesTab);
            teacherTabPane.getTabs().remove(editTab);
        });
        second.start();

        Platform.runLater(this::setupListeners);
        Platform.runLater(this::addTabListener);




    }

//    FIRST TAB METHODS
    private void loadCourses()
    {
        // Set the cell value factories using the correct property names
        nrCourseColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        depIDColumn.setCellValueFactory(new PropertyValueFactory<>("DID")); // Update to "DID" or "departmentId"
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("CID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("CUN"));

        try {
            LoadFromDatabase.getCoursesForTeacher(teacherCoursesTable);

        } catch (RuntimeException e) {
            // Handle the runtime exception gracefully
            errorAlert.setTitle("!!Error!!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(" failed to load the courses");
            errorAlert.showAndWait();
            System.err.println(e.getMessage());
        }
    }

    private void rowSelected()
    {
        Platform.runLater(()->{
            teacherCoursesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TeacherCourse>() {

                @Override
                public void changed(ObservableValue<? extends TeacherCourse> observableValue, TeacherCourse oldSelection, TeacherCourse newSelection) {
                    if (newSelection != null) {
                        teacherTabPane.getTabs().add(editTab);
                        handleCourseTableRowSelection(newSelection);

                    }
                }
            });
            studentsByCourseTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentParticipation>() {

                @Override
                public void changed(ObservableValue<? extends StudentParticipation> observableValue, StudentParticipation studentParticipation, StudentParticipation t1)
                {
                    if (t1 != null) {
                        teacherTabPane.getTabs().add(addGradesTab);
                        handleStudentsByCourseSelection(t1);
                    }
                }

            });
        });

    }

//    SECOND TAB METHODS
    private void handleCourseTableRowSelection(TeacherCourse selectedCourse)
    {
        teacherTabPane.getSelectionModel().select(editTab);
        courseIDSelected = selectedCourse.getCID();
        System.out.println(courseIDSelected);
        getStudentsForCourse();

    }

    private void handleStudentsByCourseSelection(StudentParticipation studentParticipation)
    {
        studentIDSelected =  studentParticipation.getSID();
        teacherTabPane.getTabs().add(addGradesTab);
        teacherTabPane.getSelectionModel().select(addGradesTab);

        studentAndCourseIDLabel.setText("Course ID : " + courseIDSelected + "| Student ID : " + studentIDSelected);

    }
    private void getStudentsForCourse()
    {
        studentCourseIDColumn.setCellValueFactory(new PropertyValueFactory<>("CID"));
        studentCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("CUN"));
        studentCourseStudentIDColumn.setCellValueFactory(new PropertyValueFactory<>("SID"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        studentSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        studentCourseProgramColumn.setCellValueFactory(new PropertyValueFactory<>("PROGRAM"));
        try {
            LoadFromDatabase.getStudentsByCourse(studentsByCourseTable,courseIDSelected);

        } catch (RuntimeException e) {
            // Handle the runtime exception gracefully
            errorAlert.setTitle("Loading Error");
            errorAlert.setHeaderText("Error loading students.");
            errorAlert.show();
            System.err.println(e.getMessage());
        }
    }


//  GRADES
    private void setSpinnerValues()
    {

        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3,1));
        creditsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,4));
        seminarSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,0));
        midtermSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,30,0));
        projectSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,1));
        examSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,40,1));

    }

    @FXML
    private void addNewGrade(ActionEvent event)
    {

        errorAlert.setTitle("Submission Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText("Sorry, the data could not be submitted.");

        try
        {
            DatabaseEditor.addStudentGrade(yearSpinner.getValue(),creditsSpinner.getValue(),seminarSpinner.getValue(),
                    midtermSpinner.getValue(), projectSpinner.getValue(), examSpinner.getValue(),Integer.parseInt(studentIDSelected),
                    courseIDSelected);
            showNotification(teacherPane,"Grade Subnitted.");
            teacherTabPane.getTabs().remove(addGradesTab);
            teacherTabPane.getTabs().remove(editTab);
            teacherTabPane.getSelectionModel().select(0);
        } catch (Exception e)
        {
            errorAlert.showAndWait();

        }
    }

    private void addSpinerFocusListner(Spinner<?> spinner)
    {

        spinner.getEditor().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.TAB) {
                spinner.getEditor().selectAll();
            }
        });
        spinner.getEditor().setOnMouseClicked(mouseEvent -> spinner.getEditor().selectAll());
    }

    private void setupListeners()
    {
        addSpinerFocusListner(yearSpinner);
        addSpinerFocusListner(seminarSpinner);
        addSpinerFocusListner(creditsSpinner);
        addSpinerFocusListner(examSpinner);
        addSpinerFocusListner(projectSpinner);
        addSpinerFocusListner(midtermSpinner);
    }

    private void addTabListener()
    {

        teacherTabPane.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
           if(addGradesTab.isSelected())
           {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    addGradeButton.fire();
                else if(keyEvent.getCode() == KeyCode.ESCAPE)
                    teacherTabPane.getTabs().remove(addGradesTab);

           }else if (detailsTab.isSelected()) {
               if (keyEvent.getCode() == KeyCode.ENTER) {
                   alterbutton.fire();
               }
           }else if (editTab.isSelected()) {
               if (keyEvent.getCode() == KeyCode.ESCAPE) {
                   teacherTabPane.getTabs().remove(editTab);

               }
           }
        });


    }





    //    DETAILS
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
        loadDatePicker.disableProperty().setValue(false);

        switch (elementChooser.getValue())
        {
            case TEACHERS_PASSWORD:
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
            case HIRE_DATE:
                loadDatePicker.setVisible(true);
                loadDatePicker.disableProperty().setValue(true);

                loadDataField.setVisible(false);
                countryChoiceBox.setVisible(false);
                genderChoiceBox.setVisible(false);
                passwordField.setVisible(false);

                newDataLabel.setText("Send Request to ADMIN");
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

    public void loadNewData(ActionEvent actionEvent)
    {
        TeacherField selectedField = elementChooser.getValue();

        try {
            switch (selectedField) {
                case TEACHERS_PASSWORD:
                    elementGottenFromLoadField = passwordField.getText();
                    new DatabaseEditor(TeacherField.TEACHERS_PASSWORD, elementGottenFromLoadField);
                    break;
                case COUNTRY:
                    elementGottenFromLoadField = countryChoiceBox.getValue().toString();
                    new DatabaseEditor(TeacherField.COUNTRY, elementGottenFromLoadField);
                    break;
                case GENDER:
                    elementGottenFromLoadField = genderChoiceBox.getValue().toString();
                    new DatabaseEditor(TeacherField.GENDER, elementGottenFromLoadField);
                    break;
                case FIRST_NAME:
                    elementGottenFromLoadField = loadDataField.getText();
                    new DatabaseEditor(TeacherField.FIRST_NAME, elementGottenFromLoadField);
                    break;
                case LAST_NAME:
                    elementGottenFromLoadField = loadDataField.getText();
                    new DatabaseEditor(TeacherField.LAST_NAME, elementGottenFromLoadField);
                    break;
                case EMAIL:
                    elementGottenFromLoadField = loadDataField.getText();
                    new DatabaseEditor(TeacherField.EMAIL, elementGottenFromLoadField);
                    break;
                case ADDRESS:
                    elementGottenFromLoadField = loadDataField.getText();
                    new DatabaseEditor(TeacherField.ADDRESS, elementGottenFromLoadField);
                    break;
                case PHONE_NUMBER:
                    elementGottenFromLoadField = loadDataField.getText();
                    new DatabaseEditor(TeacherField.PHONE_NUMBER, elementGottenFromLoadField);
                    break;
                case DATE_OF_BIRTH:
                    elementGottenFromLoadField = loadDatePicker.getValue().toString();
                    new DatabaseEditor(TeacherField.DATE_OF_BIRTH, elementGottenFromLoadField);
                    break;
                case HIRE_DATE:
                    elementGottenFromLoadField = loadDatePicker.getValue().toString();
                    new DatabaseEditor(TeacherField.HIRE_DATE, elementGottenFromLoadField);
                default:
                    elementGottenFromLoadField = loadDataField.getText();
                    System.out.println(elementGottenFromLoadField);
                    break;
            }
            showNotification(teacherPane,"New Data Subnitted.");

            clearAllFields();

            LoadFromDatabase.getTeacherDataFromDB(LoadFromDatabase.getTeacherEmail(), LoadFromDatabase.getTeacherPassword());
            displayData(LoadFromDatabase.getAllInfoTeacher(), LoadFromDatabase.getTeacherFirstName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
