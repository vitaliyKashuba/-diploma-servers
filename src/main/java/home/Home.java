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
import jssc.*;

/**
 * recieving data from arduino and sending it to InterlayerServer
 * recieving command from InterlayerServer and send it to arduino
 */
public class Home
{
    private static final String ip = "192.168.10.101"; // change to ip of interlayer server
    private static final int port = 1234;
    
    private static boolean armed = false;
    
    //static ObjectInputStream input;
    static ObjectOutputStream output;
    
    static BufferedReader input;
    
    static SerialPort serialPort;
    static String serialPortID = "COM5";
    
    public static void main(String args[])
    {
        System.out.println("home server started");
        try 
        {
            //read config
            
            //connect to arduino device
            serialPort = new SerialPort(serialPortID);
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_38400, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.addEventListener(new SerialPortReader(), SerialPort.MASK_RXCHAR);
            
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
        } catch (SerialPortException ex) {
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
     * method to parse controller message
     * 
     * message template on 20.05.16 18:06 is lt###_h##_t##_mt#_rl#
     * where 'lt' is light level, 'h' is hudimity, 't' is temperaature 
     * 'mt' is motion detect, 'rl' is relay status
     * 
     * calls from SerialPortReader event listener
     * @param msg message from arduino
     */
    static void controllerMessageParsing(String msg)
    {
        System.out.println("parsing " + msg);
        String values[] = msg.split("_");
        //System.out.println(values.length);
        for(String s: values)
        {
            System.out.println(s);
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
