/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import java.util.logging.Level;
import java.util.logging.Logger;
import message.Commands;

/**
 * switch on light if motion detected and light level is low
 */
public class MotionLightSwitch extends Thread
{
    @Override
    public void run()
    {
        while(true)
        {
            try 
            {
                if (Home.isLightShouldBeTurnedOn())
                {
                    Home.sendCommand(Commands.LIGHT_ON);
                }
                Thread.sleep(Home.LIGHT_SWITCH_DELAY);
                Home.sendCommand(Commands.LIGHT_OFF); //TODO fix it
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(MotionLightSwitch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
