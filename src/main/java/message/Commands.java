package message;

/**
 * Contains available commands to send to controller
 * created to avoid errors with strings-transmitting, int command minimize risk of partial send
 */
public enum Commands 
{
    LIGHT_ON(11),
    LIGHT_OFF(10);
    
    private int command;

    private Commands(int command) 
    {
        this.command = command;
    }  
    
    public int getCommandDigit()
    {
        return command;
    }
}
