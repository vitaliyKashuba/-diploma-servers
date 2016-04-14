package message;

import java.io.Serializable;

/**
 * used to send serialized data from home to interlayerServer
 */
public class Message implements Serializable
{
    //some data and get-methods
    private float temperature;
    private byte armStatus; // change to enum?
    
    public float getTemperature()
    {
        return temperature;
    }
    
    public byte getArmStatus()
    {
        return armStatus;
    }
    
    public Message() //just to test connection, needs to be rewrited
    {
        temperature = 20;
        armStatus = 1;
    }
     
}