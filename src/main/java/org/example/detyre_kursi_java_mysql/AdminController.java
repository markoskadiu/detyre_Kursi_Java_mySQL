package org.example.detyre_kursi_java_mysql;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.example.detyre_kursi_java_mysql.admin.StudentList;
import org.example.detyre_kursi_java_mysql.admin.TeacherList;
import org.example.detyre_kursi_java_mysql.database.DatabaseEditor;
import org.example.detyre_kursi_java_mysql.database.LoadFromDatabase;
import org.example.detyre_kursi_java_mysql.enums.Country;
import org.example.detyre_kursi_java_mysql.enums.Gender;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class AdminController  implements Initializable {

//    WINDOW
    @FXML private ImageView backgroundImage;
    @FXML private Button logout;
    @FXML private AnchorPane adminPane;
    @FXML private Label errorLabel;
    @FXML private TabPane adminTabPane;
    @FXML private Tab teacherTab;
    @FXML private Tab registerTab;

//    First Tab TEACHERS
    ObservableList<TeacherList> teacherData;
    @FXML private TableView<TeacherList> teacherTable;
    @FXML private TableColumn<TeacherList,Integer> teacherIDCol;
    @FXML private TableColumn<TeacherList, String> teacherDepCol;
    @FXML private TableColumn<TeacherList, String> teacherNamCol;
    @FXML private TableColumn<TeacherList, String> teacherSurCol;
    @FXML private TableColumn<TeacherList, Gender> teacherGenCol;
    @FXML private TableColumn<TeacherList, Date> teacherBDCol;
    @FXML private TableColumn<TeacherList, Date> teacherRegCol;
    @FXML private TableColumn<TeacherList, String> teacherAddCol;
    @FXML private TableColumn<TeacherList, Country> teacherCouCol;
    @FXML private TableColumn<TeacherList, String> teacherEmaCol;
    @FXML private TableColumn<TeacherList, String> teacherNumCol;
    @FXML private TableColumn<TeacherList, String> teacherPassCol;
    @FXML private TextField teacherSearch;

//   Second Tab Students
    ObservableList<StudentList> studentData;
    @FXML private TextField searchField;
    @FXML private Label searchLabel;

//  Student TABLE
    @FXML private Tab studentListTab;
    @FXML private TableView<StudentList> studentsTable;
    @FXML private TableColumn<StudentList,Integer> studentIDClumn;
    @FXML private TableColumn<StudentList,String> studentNameCoulumn;
    @FXML private TableColumn<StudentList,String> StudentSurnameCoulumn;
    @FXML private TableColumn<StudentList, Date> StudentBDColumn;
    @FXML private TableColumn<StudentList,String> StudentGenderColumn;
    @FXML private TableColumn<StudentList,String> StrudentAddressColumn;
    @FXML private TableColumn<StudentList,String> StrudentCountryColumn;
    @FXML private TableColumn<StudentList,String> studentEmailColumn;
    @FXML private TableColumn<StudentList,String> studentPhoneColumn;
    @FXML private TableColumn<StudentList,Date> studentRegistrationColumn;
    @FXML private TableColumn<StudentList,String> StudentProgramColumn;
    @FXML private TableColumn<StudentList,String> studentPasswordColumn;

//    Third Tab Register Student
    @FXML private Button signUpButton;
    @FXML private TextField userNameField;
    @FXML private TextField userLastName;
    @FXML private TextField emailField;
    @FXML private TextField numberField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Gender> genderField;
    @FXML private ChoiceBox<Country> countryField;
    @FXML private DatePicker birthDateField;
    @FXML private DatePicker registrationDateField;
    @FXML private ChoiceBox<String> studyProgramBox;
    @FXML private ChoiceBox<String> uniBox;
    private final List<String> programList = new ArrayList<>();
//    /END ATTRIBUTES-------------------------------------------------------------

//    GENERAL METHODS----------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
         backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/matrix.jpg"))));
        // Student tab initialisations
        doTeacherStuff();



// Method to generate a random color
        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                tabListener();
                doStudentStuff();
                initialiseSearch();
                setColumnsThatApplyCopy();
                LoadFromDatabase.getAllStudyPrograms(programList);
                studyProgramBox.getItems().addAll(programList);
                genderField.getItems().addAll(Gender.values());
                countryField.getItems().addAll(Country.values());
                discoLabel();
                uniBox.getItems().add("UET");

            }
        });

    }

    private Color getRandomColor(Random random) {
        int red = random.nextInt(256); // Random value between 0 and 255 for red
        int green = random.nextInt(256); // Random value between 0 and 255 for green
        int blue = random.nextInt(256); // Random value between 0 and 255 for blue
        return Color.rgb(red, green, blue);
    }

    private void discoLabel(){
        Random random = new Random();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(searchLabel.textFillProperty(), searchLabel.getTextFill())),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(searchLabel.textFillProperty(), getRandomColor(random), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(searchLabel.textFillProperty(), getRandomColor(random), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(2.5),
                        new KeyValue(searchLabel.textFillProperty(), Color.WHITE,Interpolator.EASE_BOTH))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML private void logout(ActionEvent event) throws IOException
    {
        if (event.getSource() == logout){
            new StudentManagement().logOut(event);
        }
    }

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
        notificationPane.setLayoutX(550);
        notificationPane.setLayoutY(390);
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

    private void initialiseSearch()
    {
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> studentsTable.setItems(filterData(newValue)));

        teacherSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> teacherTable.setItems(filterTeacherData(newValue)));
    }

    private <T, E> void copyData(TableColumn<T, E> column) {
        column.setCellFactory(col -> {
            TableCell<T, E> cell = new TableCell<T, E>() {
                @Override
                protected void updateItem(E item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item != null ? item.toString() : null);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    E cellValue = cell.getItem();
                    if (cellValue != null) {
                        String stringValue = cellValue.toString();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(stringValue);
                        showNotification(adminPane, "Copied: " + stringValue);
                        Clipboard.getSystemClipboard().setContent(content);
                        cell.setStyle("-fx-background-color: lightblue;");
                        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                        pause.setOnFinished(e -> cell.setStyle("-fx-background-color: transparent;"));
                        pause.play();
                    }
                }
            });

            return cell;
        });
    }

    private void setColumnsThatApplyCopy() {
        // Apply copyData to all student table columns
        for (TableColumn<StudentList, ?> column : studentsTable.getColumns()) {
            copyData(column);
        }

        // Apply copyData to all teacher table columns
        for (TableColumn<TeacherList, ?> column : teacherTable.getColumns()) {
            copyData(column);
        }
    }

   private void tabListener(){
       adminTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

           @Override
           public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1)
           {
               if (t1 == teacherTab){
                  teacherSearch.setVisible(true);
                  searchField.setVisible(false);
                  searchLabel.setVisible(true);
               }
               else if (t1 == studentListTab){
                   teacherSearch.setVisible(false);
                   searchField.setVisible(true);
                   searchLabel.setVisible(true);
               }
               else if (t1 == registerTab){
                   teacherSearch.setVisible(false);
                   searchField.setVisible(false);
                   searchLabel.setVisible(false);
               }
           }
       });
   }



    //    Teachers TAB
    private void assignTeacherColums(){
        teacherIDCol.setCellValueFactory(new PropertyValueFactory<>("TID"));
        teacherDepCol.setCellValueFactory(new PropertyValueFactory<>("DPN"));
        teacherNamCol.setCellValueFactory(new PropertyValueFactory<>("TFN"));
        teacherSurCol.setCellValueFactory(new PropertyValueFactory<>("TLN"));
        teacherBDCol.setCellValueFactory(new PropertyValueFactory<>("TBD"));
        teacherRegCol.setCellValueFactory(new PropertyValueFactory<>("TRD"));
        teacherGenCol.setCellValueFactory(new PropertyValueFactory<>("TG"));
        teacherAddCol.setCellValueFactory(new PropertyValueFactory<>("TADD"));
        teacherCouCol.setCellValueFactory(new PropertyValueFactory<>("TC"));
        teacherEmaCol.setCellValueFactory(new PropertyValueFactory<>("TEM"));
        teacherNumCol.setCellValueFactory(new PropertyValueFactory<>("TN"));
        teacherPassCol.setCellValueFactory(new PropertyValueFactory<>("TP"));
        LoadFromDatabase.getAllTeachers(teacherTable);
    }

    private ObservableList<TeacherList> filterTeacherData(String filter)
    {
        ObservableList<TeacherList> filteredData = FXCollections.observableArrayList();
        for (TeacherList teacher : teacherData) {
            if (teacher.getTFN().toLowerCase().contains(filter.toLowerCase()) ||
                teacher.getTLN().toLowerCase().contains(filter.toLowerCase()) ||
                teacher.getDPN().toLowerCase().contains(filter.toLowerCase()))
            {
                filteredData.add(teacher);
            }
        }
        return filteredData;
    }

    private void doTeacherStuff()
    {
        assignTeacherColums();
        teacherData = teacherTable.getItems();
    }


//    Second TAB STUDENTS

    private void populateStudentList()
    {
        studentIDClumn.setCellValueFactory(new PropertyValueFactory<>("SID"));
        studentNameCoulumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        StudentSurnameCoulumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        StudentBDColumn.setCellValueFactory(new PropertyValueFactory<>("Birthdate"));
        StudentGenderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        StrudentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        StrudentCountryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        studentRegistrationColumn.setCellValueFactory(new PropertyValueFactory<>("Registration"));
        StudentProgramColumn.setCellValueFactory(new PropertyValueFactory<>("Program"));
        studentPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("Password"));
        LoadFromDatabase.getAllStudents(studentsTable);
        StudentBDColumn.setVisible(false);
        StrudentAddressColumn.setVisible(false);
        StrudentCountryColumn.setVisible(false);
        studentPhoneColumn.setVisible(false);
        studentPasswordColumn.setVisible(false);

    }


    private ObservableList<StudentList> filterData(String filter)
    {
        ObservableList<StudentList> filteredData = FXCollections.observableArrayList();
        for (StudentList student : studentData) {
            if (student.getName().toLowerCase().contains(filter.toLowerCase()) ||
                    student.getSurname().toLowerCase().contains(filter.toLowerCase())) {
                filteredData.add(student);
            }
        }
        return filteredData;
    }

    private void doStudentStuff()
    {
        populateStudentList();
        studentData = studentsTable.getItems();
    }




//  THIRD TAB REGISTER------------------------------------------------------------------------

    public void signUp(ActionEvent event)
    {
        genderField.setValue(Gender.Male);
        countryField.setValue(Country.Albania);
        birthDateField.setValue(LocalDate.EPOCH);
        registrationDateField.setValue(LocalDate.now());
        studyProgramBox.setValue("Informatikë ekonomike");

        String firstName = userNameField.getText();
        String lastName = userLastName.getText();
        String email = emailField.getText();
        String number = numberField.getText();
        String address = addressField.getText();
        String password = passwordField.getText();
        String gender = genderField.getValue().toString();
        String country = countryField.getValue().toString();
        String birthDate = birthDateField.getValue().toString();
        String registrationDate = registrationDateField.getValue().toString();
        String studyProgram = switch (studyProgramBox.getValue()){
            case "Administrim Biznesi":
                yield "1";
            case "Financë-Kontabilitet":
                yield "2";
            case "Marketing":
                yield "3";
            case "Informatikë ekonomike":
                yield "4";
            case "Drejtësi":
                yield "5";
            case "Shkencë Politike":
                yield "6";
            case "Marrëdhënie Ndërkombëtare":
                yield "7";
            case "Edukim":
                yield "8";
            case "Sociologji":
                yield "9";
            case "Psikologji":
                yield "10";
            case "Arte Pamore":
                yield "11";
            case "Shkencë Kompjuterike":
                yield "12";
            case "Inxhinieri Softueri":
                yield "13";
            case "Inxhinieri Civile":
                yield "14";
            case "Arkitekturë":
                yield "15";
            case "Inxhinieri Biomedicinale":
                yield "16";
            case "Informatikë Mjekësore":
                yield "17";
            case "Menaxhimi i Kujdesit Shëndetësor":
                yield "18";
            default:
                yield "Code not found";
        };

        try {
            DatabaseEditor.registerAStudent(signUpButton, firstName, lastName, birthDate, gender, address, country, email,
                    password, number, registrationDate, studyProgram);
            showNotification(adminPane,"Succesfull Registration");
            Platform.runLater(new Runnable() {
                @Override
                public void run()
                {
                    showNotification(adminPane,"Wellcome on board "+ firstName);
                }
            });

        }catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong");
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }


}
