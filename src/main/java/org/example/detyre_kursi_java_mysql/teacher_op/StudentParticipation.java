package org.example.detyre_kursi_java_mysql.teacher_op;

public class StudentParticipation {
    private final String CID;
    private final String CUN;
    private final String SID;
    private final String SURNAME;
    private final String NAME;
    private final String PROGRAM;


    public StudentParticipation(String cid, String cun, String sid, String surname, String name, String program)
    {
        CID = cid;
        CUN = cun;
        SID = sid;
        SURNAME = surname;
        NAME = name;
        PROGRAM = program;
    }

    public String getCID()
    {
        return CID;
    }

    public String getCUN()
    {
        return CUN;
    }

    public String getNAME()
    {
        return NAME;
    }
    public String getSURNAME(){
        return SURNAME;
    }
    public String getPROGRAM(){
        return PROGRAM;
    }

    public String getSID()
    {
        return SID;
    }
}
