package interlayerServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteDeviceConnector extends Thread
{
    ObjectInputStream input; 
    ObjectOutputStream output; 
    
    public RemoteDeviceConnector(Socket socket) throws IOException
    {        
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        
        start();
    }
    
    @Override
    public void run()
    {
        //read command
        //do something
    }
}
