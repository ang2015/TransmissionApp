package alvin.transmission;

public class MyMessage
{
    enum MessageType
    {
        NEW
        {
            @Override
            public String toString()
            {
                return "NEW";
            }
        }
    }

    private MessageType messageType;
    private String fixedString;
    private String variableString;
    private long longVal;
    private int intVal;
    private boolean boolVal;

    MyMessage()
    {
        messageType = MessageType.NEW;
        fixedString = "FIXEDSTR";
        variableString = "variable_string_initial_value";
        longVal = Long.MAX_VALUE;
        intVal = Integer.MAX_VALUE;
        boolVal = true;
    }

    void setVariableString(String val)
    {
        variableString = val;
    }

    String getVariableString()
    {
        return variableString;
    }

    void copyFrom(MyMessage val)
    {
        messageType = val.messageType;
        fixedString = val.fixedString;
        variableString = val.variableString;
        longVal = val.longVal;
        intVal = val.intVal;
        boolVal = val.boolVal;
    }

    void dump(String header)
    {
        System.out.println(header + messageType.toString() + "," +
                           fixedString + "," +
                           longVal + "," +
                           variableString + "," +
                           intVal + "," +
                           boolVal);
    }
}
