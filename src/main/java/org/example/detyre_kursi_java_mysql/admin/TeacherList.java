package org.example.detyre_kursi_java_mysql.admin;

import org.example.detyre_kursi_java_mysql.enums.Country;
import org.example.detyre_kursi_java_mysql.enums.Gender;

import java.util.Date;

public class TeacherList {
    private final int TID; // teacher id
    private final String DPN;// department name
    private final String TFN; // teacher first name
    private final String TLN;// teacher last name
    private final Date TBD; // date of birth
    private final Date TRD;// registration date
    private final Gender TG; // gender
    private final String TADD; // address
    private final Country TC; // country
    private final String TEM; // email address
    private final String TN; // phone number
    private final String TP; // teacher password

    public TeacherList(int TID, String DPN, String TFN, String TLN, Date TBD, Date TRD, Gender TG, String TADD, Country TC, String TEM, String TN, String TP)
    {
        this.TID = TID;
        this.DPN = DPN;
        this.TFN = TFN;
        this.TLN = TLN;
        this.TBD = TBD;
        this.TRD = TRD;
        this.TG = TG;
        this.TADD = TADD;
        this.TC = TC;
        this.TEM = TEM;
        this.TN = TN;
        this.TP = TP;
    }

    public int getTID()
    {
        return TID;
    }

    public Country getTC()
    {
        return TC;
    }

    public Date getTBD()
    {
        return TBD;
    }

    public Date getTRD()
    {
        return TRD;
    }

    public Gender getTG()
    {
        return TG;
    }

    public String getDPN()
    {
        return DPN;
    }

    public String getTADD()
    {
        return TADD;
    }

    public String getTEM()
    {
        return TEM;
    }

    public String getTFN()
    {
        return TFN;
    }

    public String getTLN()
    {
        return TLN;
    }

    public String getTN()
    {
        return TN;
    }

    public String getTP()
    {
        return TP;
    }


}
