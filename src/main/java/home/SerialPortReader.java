package home;

import static home.Home.serialPort;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortReader implements SerialPortEventListener
{
    @Override
    public void serialEvent(SerialPortEvent event) 
    {
        if(event.isRXCHAR() && event.getEventValue() > 0)
        {
            try 
            {
                String data = serialPort.readString(event.getEventValue());
                Thread.sleep(1000); //increase baudrate or delay if data not transmissed in full-size
                //System.out.println(data);
                Home.controllerMessageParsing(data);
            } catch (SerialPortException ex) 
            {
                Logger.getLogger(SerialPortReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialPortReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
