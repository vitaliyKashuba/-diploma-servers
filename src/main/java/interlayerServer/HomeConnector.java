package interlayerServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * thread used to recieve data from home
 */
public class HomeConnector extends Thread
{
    private ObjectInputStream input; 
    //private ObjectOutputStream output; 
    
    PrintWriter output;
    
    public HomeConnector(Socket socket) throws IOException
    {        
        input = new ObjectInputStream(socket.getInputStream());
        //output = new ObjectOutputStream(socket.getOutputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        
        start();
    }
    
    /**
     * send message to home
     * called from InterlayerServer
     * @param message
     * @throws IOException 
     */
    public void sendMessageToHome(String message) throws IOException
    {
        System.out.println("homeConnector.send");
        //output.writeBytes(message);
        output.println(message);
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
                System.out.println("home server disconnected");
                InterlayerServer.reconnectHome();
                //this.destroy();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            //listen to web-server commands
        }
    }
}
