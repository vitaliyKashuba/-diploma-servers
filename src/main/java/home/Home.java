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
public class Home
{
    static final String ip = "192.168.10.101";
    static final int port = 1234;
    
    static boolean armed = false;
    
    //static ObjectInputStream input;
    static ObjectOutputStream output;
    
    static BufferedReader input;
    
    public static void main(String args[])
    {
        System.out.println("home server started");
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
            MessagesSender ms = new MessagesSender(output);// runs in new thread
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
                while((s=input.readLine())!=null)
                {
                    System.out.println(s);
                    commandHandler(s);
                }
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * choose what method call to send command to arduino
     * @param command 
     */
    static void commandHandler(String command)
    {
        if (command.startsWith("turnServo")) //command pattern turnServo###
        {
            turnServo(Integer.parseInt(command.substring(9, command.length())));
        }
        else
        {
            switch(command)
            {
                case "photo":
                    takePhoto();
                    break;
                default:
                    System.out.println("cannot parse command");
                    break;
            }
        }
    }
    
    /**
     * send command to turn servo with web-cam
     * @param degrees 
     */
    static void turnServo(int degrees)
    {
        //TODO send it to arduino
    }
    
    /**
     * take photo on web-cam and upload it somewhere
     */
    static void takePhoto()
    {
        //TODO finish it
    }
    
    /**
     * starts alarm
     * should activate if sensors detects motion or user send command
     */
    static void alarmOn()
    {}
    
    /**
     * ends alarm
     * called by user command or after 5 minutes of alarming
     */
    static void alarmOff()
    {}
       
}
