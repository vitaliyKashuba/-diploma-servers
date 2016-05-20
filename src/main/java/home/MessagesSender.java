package home;

import static home.Home.output;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * become useless ?
 */
public class MessagesSender extends Thread
{
    private ObjectOutputStream output;
    
    public MessagesSender(ObjectOutputStream output)
    {
        this.output=output;
        start();
    }
    
    @Override
    public void run()
    {
        // read data from arduino
        while (true)
        {
            //System.out.println("run");
            Message message = new Message(); // write data to this message
            try 
            {
                output.writeObject(message);
                //System.out.println("message sent");
                Thread.sleep(2000);
            } catch (IOException ex) 
            {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("sending message failed");
            } catch (InterruptedException ex) 
            {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
    }
}
