package interlayerServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * thread used to recieve data from home
 */
public class HomeConnector extends Thread
{
    ObjectInputStream input; 
    ObjectOutputStream output; 
    
    public HomeConnector(Socket socket) throws IOException
    {        
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        
        start();
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            try 
            {
                InterlayerServer.homeMessageHandler((Message)input.readObject());
            } catch (IOException ex) {
                Logger.getLogger(HomeConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            //listen to web-server commands
        }
    }
}
