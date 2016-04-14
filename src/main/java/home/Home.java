package home;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * recieving data from arduino and sending it to InterlayerServer
 * recieving command from InterlayerServer and send it to arduino
 */
public class Home implements Runnable
{
    static final String ip = "localhost";
    static final int port = 1234;
    
    //static ObjectInputStream input;
    static ObjectOutputStream output;
    
    static BufferedReader input;
    
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
            //input = new ObjectInputStream(socket.getInputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            System.out.println("inputs created");
            
            //create new thread to read data and messages to interlayer
            Runnable r = new Home();
            //r.run();
            
            String s;
            /**
             * recieving messages from interlayer
             */
            while (true)
            {
                //wait for message

                //pause messenger thread

                //send cmd to arduino

                //start messenger thread
                while((s=input.readLine())!=null)//add handler for recieved command
                {
                    System.out.println(s);
                }
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * read data from arduino and send it to interlayer every 2 seconds in thread
     */
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
