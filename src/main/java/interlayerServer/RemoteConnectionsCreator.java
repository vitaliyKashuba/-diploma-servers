package interlayerServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * used to create connections from remote device at same time
 * just ad-hoc to avoid server falling after home disconnect
 */
public class RemoteConnectionsCreator extends Thread
{
    ServerSocket remoteDeviceSocket;
    static int remoteConnectionsCount = 0;
    
    public RemoteConnectionsCreator(ServerSocket remoteDeviceSocket)
    {
        this.remoteDeviceSocket = remoteDeviceSocket;
        //start();
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            try 
            {
                Socket remoteSocket = remoteDeviceSocket.accept();
                System.out.println("remote device connected");
                remoteConnectionsCount++; //TODO -- while remote disconnected
                RemoteDeviceConnector remoteConnector = new RemoteDeviceConnector(remoteSocket);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(RemoteConnectionsCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
