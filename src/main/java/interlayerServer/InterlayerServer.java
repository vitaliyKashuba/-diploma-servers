package interlayerServer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import message.Message;

/**
 * transfering data between home and web-server
 * transfering command between remote device\web-server and home
 * must have 'white' ip-adress for correct work
 */
public class InterlayerServer 
{
    private static int homeConnectionPort = 1234;
    private static int remoteDeviceConnectionPort = 1235;
    
    private static HomeConnector homeConnector;
    private static RemoteConnectionsCreator remoteConnectionsCreator;
    /**
     * de-serialize data, recieved from home and publish it on web-server
     * @param msg recieved data
     */
    static void homeMessageHandler(Message msg) //TODO finish realise it 
    {
        System.out.println(msg.getTemperature());
        //send it to web-server
        /*File f = new File("E:\\camRecieved.png"); //just to test File recieving
        try 
        {
            BufferedImage img = ImageIO.read(msg.getImage());
            ImageIO.write(img, "PNG", f);
        } catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }//test code*/
        
        /**
         * send data to remote devices if any connected
         */
        if(RemoteConnectionsCreator.remoteConnectionsCount>0)
        {
            remoteConnectionsCreator.sendToAllRemoteDevices(msg);
        }
    }
    
    /**
     * used to send home command to execute smth on arduino
     * command is simple (String or int), not needs to be serializable
     * @param message command from command list
     */
    static void sendMessageToHome(String message) throws IOException //TODO add command list
    {
        System.out.println("InterlayerServer.send "+message);
        homeConnector.sendMessageToHome(message);
    }
    
    public static void main(String[] args) 
    {
        System.out.println("interlayer started");
        try 
        {
            ServerSocket homeServerSocket = new ServerSocket(homeConnectionPort);
            ServerSocket remoteDeviceSocket = new ServerSocket(remoteDeviceConnectionPort);
            
            remoteConnectionsCreator = new RemoteConnectionsCreator(remoteDeviceSocket);
            remoteConnectionsCreator.start();
            
            while (true)
            {
                Socket homeSocket = homeServerSocket.accept();
                System.out.println("home connected");
                //connect home
                homeConnector = new HomeConnector(homeSocket);
            }
            
        } catch (IOException ex) 
        {
            System.out.println("disconnect");
        }
    }   
}
