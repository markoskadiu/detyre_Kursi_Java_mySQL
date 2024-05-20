package org.example.detyre_kursi_java_mysql;



import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.detyre_kursi_java_mysql.database.InvalidCredentialsException;
import org.example.detyre_kursi_java_mysql.database.LoadFromDatabase;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import static org.example.detyre_kursi_java_mysql.database.LoadFromDatabase.connectToDatabase;

public class LoginController implements Initializable {

    //    GENERAL
    @FXML
    private ImageView backgroundImage;
    @FXML
    private TabPane tabPane;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private StackPane notificationPane;

    //    ADMIN CREDENTIALS
    @FXML
    private Tab adminTab;
    @FXML
    private Label loginLabel2;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private Button adminLoginButton;
    @FXML
    private TextField adminEmailField;

    //    TEACHER CREDENTIALS
    @FXML
    private Tab teacherTab;
    @FXML
    private Label loginLabel1;
    @FXML
    private TextField teacherEmailField;
    @FXML
    private PasswordField teacherPasswordField;
    @FXML
    private Button teacherLoginButton;

    //    STUDENT CREDENTIALS
    @FXML
    private Tab studentTab;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField studentEmailField;
    @FXML
    private PasswordField studentPasswordField;
    @FXML
    private Button studentLoginButton;

    // Stage and Scene variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    //    OTHERS
    private String userName;
    private String password;

    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        getBackgroundImage();
        discoLabel(loginLabel);
        discoLabel(loginLabel1);
        discoLabel(loginLabel2);

        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                try {
                    connectToDatabase();
                    showNotification(loginPane, "Connected");
                } catch (Exception e) {
                    showNotification(loginPane, "Error Connecting to DataBase");
                }
            }
        });

        addKeyListeners();


        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                if (!isInternetReachable()) {
                    errorAlert.setTitle("Connection Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Could not connect to Internet");
                    ButtonType retry = new ButtonType("Retry");
                    ButtonType okButton = new ButtonType("OK");
                    ButtonType exit = new ButtonType("EXIT");

                    errorAlert.getButtonTypes().setAll(retry, okButton, exit);

                    errorAlert.showAndWait().ifPresent(response -> {
                        if (response.equals(retry)) {
                            try {
                                reloadController();
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                        if (response.equals(okButton)) {
                            errorAlert.close();
                        }
                        if (response.equals(exit)) {
                            System.exit(0);
                        }
                    });

                }
            }

        });

    }

    //    Check for internet connection
    public static boolean isInternetReachable()
    {
        try {
            // Try connecting to a reliable server (e.g., Google's DNS server)
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            socket.close();
            return true; // If connection successful, internet is reachable
        } catch (IOException e) {
            return false; // If connection fails, internet is not reachable
        }
    }

    //    Login for student using email and password to get the data from DB
    @FXML
    public void studentLogin(ActionEvent event)
    {
        userName = studentEmailField.getText();
        password = studentPasswordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            setWrongFieldBorder(1);
            showNotification(loginPane, "Please enter a valid email address or password");
            return;
        }

        try {
            // Attempt to get student data from the database
            LoadFromDatabase.getStudentDataFromDB(userName, password);
            loadStudentsScene();
        } catch (InvalidCredentialsException e) {
            setWrongFieldBorder(1);
            showNotification(loginPane, "Invalid email or password. Please try again.");
        }
    }


    //    Loader of Student management Scene
    private void loadStudentsScene()
    {
        // Load the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentManagement.fxml"));
        try {
            Parent root = loader.load();
            // Get controller of the next scene
            StudentManagement managerController = loader.getController();
            managerController.displayData(LoadFromDatabase.getAllInfoStudent(), LoadFromDatabase.getVar_studentFirstName());

            // Get the current stage
            Stage currentStage = (Stage) studentEmailField.getScene().getWindow();

            // Create a new Scene with the loaded root
            Scene scene = new Scene(root);

            // Set the new Scene to the stage
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the Student Management scene", e);
        }
    }


    //    Login for student using email and password to get the data from DB
    public void teacherLogin(ActionEvent event)
    {
        userName = teacherEmailField.getText();
        password = teacherPasswordField.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            setWrongFieldBorder(2);
            return;
        }

        try {
            LoadFromDatabase.getTeacherDataFromDB(userName, password);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherManagement.fxml"));
            root = loader.load();

            // Get controller of Scene2
            TeacherController managerController = loader.getController();
            managerController.displayData(LoadFromDatabase.getAllInfoTeacher(), LoadFromDatabase.getTeacherFirstName());

            // Get the current stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new Scene with loaded root
            scene = new Scene(root);

            // Set the new Scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (InvalidCredentialsException e) {
            setWrongFieldBorder(2);
            showNotification(loginPane, "Please enter a valid email address or password");
        } catch (IOException e) {
            errorAlert.setTitle("x)");
            errorAlert.setContentText("Error has occurred" + e.getCause());
            errorAlert.show();
            throw new RuntimeException(e);
        }
    }

    //    Login for student using email and password to get the data from DB
    public void adminLogin(ActionEvent event) throws IOException
    {
        userName = adminEmailField.getText();
        password = adminPasswordField.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            setWrongFieldBorder(2);
            return;
        }

        if (userName.equals("admin") && password.equals("admin")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminManagement.fxml"));
            root = loader.load();

            // Get controller of Scene2
            AdminController adminController = loader.getController();


            // Get the current stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new Scene with loaded root
            scene = new Scene(root);

            // Set the new Scene to the stage
            stage.setScene(scene);
            stage.show();
        } else {
            showNotification(loginPane, "Please enter a valid email address or password");
            setWrongFieldBorder(2);
        }

    }

    //    Methods to make label change color
    private Color getRandomColor()
    {
        Random random = new Random();
        int red = random.nextInt(256); // Random value between 0 and 255 for red
        int green = random.nextInt(256); // Random value between 0 and 255 for green
        int blue = random.nextInt(256); // Random value between 0 and 255 for blue
        return Color.rgb(red, green, blue);
    }

    private void discoLabel(Label label)
    {

        Color color = getRandomColor();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(label.textFillProperty(), color)),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(5),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(6),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(7),
                        new KeyValue(label.textFillProperty(), getRandomColor(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(8),
                        new KeyValue(label.textFillProperty(), color, Interpolator.EASE_BOTH))
        );
        timeline.setCycleCount(100);
        timeline.play();
    }

    //    Method to send an email in case of forgetting credentials
    public void forgotPassword(ActionEvent actionEvent)
    {
        if (Desktop.isDesktopSupported()) {
            String recipientEmail = URLEncoder.encode("markoskadiu600@gmail.com", StandardCharsets.UTF_8);
            String subject = URLEncoder.encode("Forgot Credentials", StandardCharsets.UTF_8).replace("+", "%20");
            String body = URLEncoder.encode("Hello! I have lost my credentials.\nMy full name is: \nMy Birthday is:",
                    StandardCharsets.UTF_8).replace("+", "%20");
            String mailTo = String.format("mailto:%s?subject=%s&body=%s", recipientEmail, subject, body);

            try {
                URI uri = new URI(mailTo);
                Desktop.getDesktop().mail(uri);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Desktop is not supported");
        }
    }

    //    Method to set the backgound images
    public void getBackgroundImage()
    {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1)
            {
                if (t1 == studentTab) {
                    backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images" +
                            "/bridge.jpg"))));
                } else if (t1 == teacherTab) {
                    backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images" +
                            "/lms.jpg"))));
                } else if (t1 == adminTab) {
                    backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images" +
                            "/matrix.jpg"))));
                }
            }
        });
    }

    //    Method to display notifications
    public void showNotification(AnchorPane root, String message)
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
                        new KeyValue(notificationPane.opacityProperty(), 0.0)),
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

    // Methods to add enter key listener to fire the login button
    private void addKeyListeners()
    {
        addEnterKeyHandler(studentEmailField, studentLoginButton);
        addEnterKeyHandler(studentPasswordField, studentLoginButton);
        addEnterKeyHandler(teacherEmailField, teacherLoginButton);
        addEnterKeyHandler(teacherPasswordField, teacherLoginButton);
        addEnterKeyHandler(adminEmailField, adminLoginButton);
        addEnterKeyHandler(adminPasswordField, adminLoginButton);
    }

    private void addEnterKeyHandler(TextField textField, Button button)
    {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                button.fire();
            }
        });
    }

    //    Method in case of an introuder
    private void setWrongFieldBorder(int fields)
    {
        switch (fields) {
            case 1:
                studentPasswordField.setStyle("-fx-border-color: red");
                studentEmailField.setStyle("-fx-border-color: red");
                break;
            case 2:
                teacherEmailField.setStyle("-fx-border-color: red");
                teacherPasswordField.setStyle("-fx-border-color: red");
                break;
            case 3:
                adminEmailField.setStyle("-fx-border-color: red");
                adminPasswordField.setStyle("-fx-border-color: red");
        }

    }

    //    Method to reload the scene and controller
    public void reloadController()
    {
        try {
            // Load the FXML file associated with this controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) backgroundImage.getScene().getWindow();

            // Create a new Scene with the loaded root
            Scene scene = new Scene(root);

            // Set the new Scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}