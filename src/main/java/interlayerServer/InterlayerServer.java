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
 * must have 'white' ip-adress for correct eork
 */
public class InterlayerServer 
{
    static int homeConnectionPort = 1234;
    static int remoteDeviceConnectionPort = 1235;
    
    /**
     * de-serialize data, recieved from home and publish it on web-server
     * @param msg recieved data
     */
    static void homeMessageHandler(Message msg) //TODO realise it 
    {
        System.out.println(msg.getTemperature());
        //send it to web-server
    }
    
    public static void main(String[] args) 
    {
        try 
        {
            //open serverSocket
            ServerSocket homeServerSocket = new ServerSocket(homeConnectionPort);
            Socket homeSocket = homeServerSocket.accept();
            System.out.println("home connected");
            //connect home
            HomeConnector homeConnector = new HomeConnector(homeSocket);
            
            ServerSocket remoteDeviceSocket = new ServerSocket(remoteDeviceConnectionPort);
            
            while (true) 
            {
                Socket remoteSocket = remoteDeviceSocket.accept();
                
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
