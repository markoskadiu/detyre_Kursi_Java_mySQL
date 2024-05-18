module org.example.detyre_kursi_java_mysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires javafx.media;


    opens org.example.detyre_kursi_java_mysql.teacher_op;
    opens org.example.detyre_kursi_java_mysql.admin;
    exports org.example.detyre_kursi_java_mysql;
    exports org.example.detyre_kursi_java_mysql.database;
    exports org.example.detyre_kursi_java_mysql.enums;
    exports org.example.detyre_kursi_java_mysql.teacher_op;
    exports org.example.detyre_kursi_java_mysql.student_op;
    exports org.example.detyre_kursi_java_mysql.admin;
    opens org.example.detyre_kursi_java_mysql;


// Add this line to export the package

}
