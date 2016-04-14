package interlayerServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteDeviceConnector extends Thread
{
    //ObjectInputStream input; 
    //ObjectOutputStream output; 
    
    BufferedReader input;
    String message;
    
    public RemoteDeviceConnector(Socket socket) throws IOException
    {        
        //input = new ObjectInputStream(socket.getInputStream());
        //output = new ObjectOutputStream(socket.getOutputStream());
        
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        start();
    }
    
    @Override
    public void run()
    {
        //System.out.println("rdc run");
        try 
        {
            //read command
            //do something
            
            while ((message = input.readLine()) != null)
            {
                System.out.println(message);
                InterlayerServer.sendMessageToHome(message);
            }
        } catch (IOException ex) 
        {
            Logger.getLogger(RemoteDeviceConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
