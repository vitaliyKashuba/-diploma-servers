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
    
    public static void main(String[] args) 
    {
        try 
        {
            //open serverSocket
            ServerSocket homeServerSocket = new ServerSocket(homeConnectionPort);
            Socket homeSocket = homeServerSocket.accept();
            System.out.println("home connected");
            //connect home
            Thread.sleep(200);
            
            ObjectInputStream homeInput = new ObjectInputStream(homeSocket.getInputStream());
            ObjectOutputStream homeOutput = new ObjectOutputStream(homeSocket.getOutputStream());
            
            //System.out.println("object streams created");
            
            while (true) 
            {
                //read data from home
                Message msg = (Message)homeInput.readObject();
                //System.out.println("get msg");
                System.out.println(msg.getTemperature());
                //send it to web-server
                //listen to android-device connections
                //listen to web-server commands
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(InterlayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
