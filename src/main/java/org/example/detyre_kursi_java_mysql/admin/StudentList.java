package org.example.detyre_kursi_java_mysql.admin;

import java.util.Date;

public class StudentList {
    private final int SID ;
    private final String Name;
    private final String Surname;
    private final Date Birthdate;
    private final String Gender ;
    private final String Address;
    private final String Country;
    private final String Email ;
    private final String Phone ;
    private final Date Registration;
    private final String Program;
    private final String Password;

    public StudentList(int SID, String Name, String Surname, Date Birthdate, String Gender, String Address,
                       String Country, String Email, String Phone, Date Registration, String Program, String Password)
    {
        this.SID = SID;
        this.Name = Name;
        this.Surname = Surname;
        this.Birthdate = Birthdate;
        this.Gender = Gender;
        this.Address = Address;
        this.Country = Country;
        this.Email = Email;
        this.Phone = Phone;
        this.Registration = Registration;
        this.Program = Program;
        this.Password = Password;
    }

    public int getSID()
    {
        return SID;
    }

    public Date getBirthdate()
    {
        return Birthdate;
    }

    public Date getRegistration()
    {
        return Registration;
    }

    public String getAddress()
    {
        return Address;
    }

    public String getCountry()
    {
        return Country;
    }

    public String getEmail()
    {
        return Email;
    }

    public String getGender()
    {
        return Gender;
    }

    public String getName()
    {
        return Name;
    }

    public String getPassword()
    {
        return Password;
    }

    public String getPhone()
    {
        return Phone;
    }

    public String getProgram()
    {
        return Program;
    }

    public String getSurname()
    {
        return Surname;
    }

}
