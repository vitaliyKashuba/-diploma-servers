package interlayerServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /**
     * de-serialize data, recieved from home and publish it on web-server
     * @param msg recieved data
     */
    static void homeMessageHandler(Message msg) //TODO finish realise it 
    {
        System.out.println(msg.getTemperature());
        //send it to web-server
    }
    
    /**
     * used to send home command to execute smth on arduino
     * command is simple (String or int), not needs to be serializable
     * @param message command from command list
     */
    static void sendMessageToHome(String message) throws IOException //TODO add command list
    {
        System.out.println("InterlayerServer.send");
        homeConnector.sendMessageToHome(message);
    }
    
    public static void main(String[] args) 
    {
        System.out.println("interlayer started");
        try 
        {
            //open serverSocket
            ServerSocket homeServerSocket = new ServerSocket(homeConnectionPort);
            Socket homeSocket = homeServerSocket.accept();
            System.out.println("home connected");
            //connect home
            homeConnector = new HomeConnector(homeSocket);
            
            ServerSocket remoteDeviceSocket = new ServerSocket(remoteDeviceConnectionPort);
            
            /*while (true) 
            {
                Socket remoteSocket = remoteDeviceSocket.accept();
                System.out.println("remote device connected");
                RemoteDeviceConnector remoteConnector = new RemoteDeviceConnector(remoteSocket);    
            }*/// this worked just uncomment if RemoteConnectionsCreator fails
            
        } catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
            /*homeConnector.destroy();
            System.out.println("destroyed");
            reconnectHome();*/
        }
    }
    
    /**
     * used to allow reconnect home server without restarting interlayer
     */
    static void reconnectHome() //TODO test it
    {
        try 
        {
            System.out.println("try to reconnect");
            homeConnector = null;
            System.out.println("nulled");
            
            ServerSocket homeServerSocket = new ServerSocket(homeConnectionPort);
            Socket homeSocket = homeServerSocket.accept();
            System.out.println("home reconnected");
            //connect home
            homeConnector = new HomeConnector(homeSocket);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
