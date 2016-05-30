package home;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import message.Message;
import jssc.*;
import message.Commands;
import javax.media.*;
import javax.media.control.*;
import javax.media.datasink.*;
import javax.media.format.*;
import javax.media.protocol.*;
import javax.swing.ImageIcon;

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
    private static ObjectOutputStream output;
    
    private static BufferedReader input;
    
    static SerialPort serialPort;
    private static String serialPortID = "COM3";
    
    private static int servoDegree = 90;
    private static boolean motion;
    private static int lightLevel;
    static final int LIGHT_LEVEL_LIMIT = 50;
    static final int LIGHT_SWITCH_DELAY = 5000;
    
    private static CaptureDeviceInfo	captureVideoDevice = null;
    private static Webcam webcam;
    
    public static void main(String args[])
    {
        //Vector deviceListVector = CaptureDeviceManager.getDeviceList(null);
        
        //System.out.println(deviceListVector.size());
        
        /*Webcam webcam = Webcam.getDefault();
        webcam.open();
        try {
            ImageIO.write(webcam.getImage(), "PNG", new File("C:\\hello-world.png"));
        } catch (IOException ex) {
            //Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        webcam = Webcam.getDefault();
        webcam.open();
        
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
            
            new MotionLightSwitch().start();
            
            //create new thread to read data and messages to interlayer
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
     * called from main cycle when some data recieved from interlayer
     * @param command 
     */
    static void commandHandler(String command)
    {
        if (command.startsWith("turnServo")) //command pattern turnServo###
        {
            turnServo(Integer.parseInt(command.substring(9, command.length())));
        } // syntax changed, become useless?
        else
        {
            switch(command)
            {
                case "lightOn":
                    setLightOn(true);
                    break;
                case "lightOff":
                    setLightOn(false);
                    break;
                case "photo":
                    takePhoto();
                    break;
                case "turnRight":
                    servoDegree = servoDegree+10;
                    turnServo(servoDegree);
                    break;
                case "turnLight":
                    servoDegree = servoDegree-10;
                    turnServo(servoDegree);
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
     * serialize parsed data in Message and call sendMessage to send it to interlayer
     * @param msg message from arduino
     */
    static void controllerMessageParsing(String msg)
    {
        String values[] = msg.split("_");

        int lightLevel = Integer.parseInt(values[0].substring(2));
        float humidity = Float.parseFloat(values[1].substring(1));
        float temperature = Float.parseFloat(values[2].substring(1));
        boolean motion = false, relayStatus = false;
        if (values[3].substring(2).equals("0"))
        {
            motion = false;
        }
        else if (values[3].substring(2).equals("1"))
        {
            motion = true;
        }
        if (values[4].substring(2).equals("0"))
        {
            relayStatus = false;
        }
        else if (values[4].substring(2).equals("1"))
        {
            relayStatus = true;
        }
        System.out.println("LightLevel: "+lightLevel+"\nHumidity: "+humidity+"\nTemperature: "+temperature+"\nmotion detected: "+motion+"\nrelayStatus: "+relayStatus+"\n");
        
        Home.lightLevel = lightLevel;
        Home.motion = motion;
        
        BufferedImage image = webcam.getImage();
        ByteArrayOutputStream byteImage = new ByteArrayOutputStream();
        try 
        {
            ImageIO.write(image, "png", byteImage);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte binariedImage[] = byteImage.toByteArray();
        
        /*ByteArrayInputStream inputImg = new ByteArrayInputStream(buf);
        try 
        {
            BufferedImage imgtest = ImageIO.read(inputImg);
            ImageIO.write(imgtest, "png", new File("E:\\cAfterByteArrayConvert.png"));
        } catch (IOException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }*/   //just how it should be on client side
        
        Message messageToSend = new Message(lightLevel, temperature, humidity, motion, relayStatus, binariedImage);
        sendMessage(messageToSend);
        
    }
    
    /**
     * send serialized message to interlayer
     */
    static void sendMessage(Message msg)
    {
        try 
        {
            output.writeObject(msg);
        } 
        catch (IOException ex) 
        {
            System.out.println("sending message to interlayer failed");
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * send digital command to controller
     * @param command command from enum Commands
     * @see Commands
     */
    static void sendCommand(Commands command)
    {
        try 
        {
            serialPort.writeBytes(Integer.toString(command.getCommandDigit()).getBytes()); //some magic
        } 
        catch (SerialPortException ex) 
        {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * send command to turn servo with web-cam
     * @param degrees 
     */
    static void turnServo(int degrees)
    {
        if(degrees > 0 && degrees < 180)
        {
            try 
            {
                serialPort.writeBytes(("s"+degrees).getBytes());
            } 
            catch (SerialPortException ex) 
            {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
    
    /**
     * switch on/off light
     * @param onOff true - light on, false - off
     */
    static void setLightOn(boolean onOff)
    {
        if (onOff)
        {
            sendCommand(Commands.LIGHT_ON);
        }
        else
        {
            sendCommand(Commands.LIGHT_OFF);
        }
    }
     
    /**
     * method used to check, is it necessary to switch on the light for a while
     * called from MotionLightSwitch in thread
     * @return true if motion detected and light level is low
     */
    static boolean isLightShouldBeTurnedOn()
    {
        if (lightLevel<LIGHT_LEVEL_LIMIT && motion == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * used to control color of RGB led backlight
     * @param on switch on\off
     * @param color rgb color 4rrrgggbbb //TODO change to Class RGB?
     */
    static void setRGB(boolean on, int color)
    {}
    
    static void feedFish()
    {}
    
}
