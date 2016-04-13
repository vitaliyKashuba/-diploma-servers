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

public class InterlayerServer 
{
    static int homeConnectionPort = 1234;
    static int remoteDeviceConnectionPort;
    
    static void homeMessageHandler(Message msg) //TODO realise it 
    {
        System.out.println(msg.getTemperature());
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
            
            while (true) 
            {
                //read data from home
                //homeMessageHandler((Message)homeInput.readObject());
                //send it to web-server
                //listen to android-device connections
                //listen to web-server commands
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
