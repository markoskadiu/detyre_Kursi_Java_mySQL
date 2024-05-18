package org.example.detyre_kursi_java_mysql.database;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message)
    {
        System.err.println("Invalid credentials provided: " + message);
    }
}
