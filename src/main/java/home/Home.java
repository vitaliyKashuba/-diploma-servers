package home;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

public class Home implements Runnable
{
    static final String ip = "localhost";
    static final int port = 1234;
    
    static ObjectInputStream input;
    static ObjectOutputStream output;
    
    public static void main(String args[])
    {
        try 
        {
            //read config
            
            //connect to arduino device
            
            //connect to interlayer
            Socket socket = new Socket(ip, port);
            System.out.println("connected");
            output = new ObjectOutputStream(socket.getOutputStream());            
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("inputs created");
            
            //create new thread to read data and messages to interlayer
            Runnable r = new Home();
            r.run();
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true)
        {
            //wait for message
            
            //pause messenger thread
            
            //send cmd to arduino
            
            //start messenger thread
        }
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
