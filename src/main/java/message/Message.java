package message;

import java.io.Serializable;

/**
 * used to send serialized data from home to interlayerServer
 */
public class Message implements Serializable
{
    //some data and get-methods
    private float temperature;
    private int pressure;
    private byte armStatus; // change to enum?
    //photo?
    
    public float getTemperature()
    {
        return temperature;
    }
    
    public byte getArmStatus()
    {
        return armStatus;
    }
    
    public int getPressure()
    {
        return pressure;
    }
    
    public Message() //just to test connection, needs to be rewrited
    {
        temperature = 20;
        armStatus = 1;
    }
    
    public Message(float tempetarure, int pressure, byte armStatus)
    {
        this.temperature=tempetarure;
        this.pressure = pressure;
        this.armStatus = armStatus;
    }
     
}
