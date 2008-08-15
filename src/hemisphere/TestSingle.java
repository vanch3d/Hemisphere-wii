/*
 * TestMultiple.java
 *
 * Created on 08 July 2008, 15:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hemisphere;

import processing.core.PApplet;
import processing.core.PAppletExt;
import surface.Hemisphere;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
/**
 *
 * @author nicolas
 */
public class TestSingle {
    
    //static Wiimote mote=null;
    //static PApplet hemisphere;//  = new Main();
   //static PApplet spherical;// = new Spherical();
    /** Creates a new instance of TestMultiple */
    static Wiimote[] wiimotes = null;
    
       
    public static void main(String[] args) {
        wiimotes = WiiUseApiManager.getWiimotes(1, true);
        System.out.println("NB DETECTED: " + wiimotes.length);
        if (wiimotes.length!= 0) 
        {
            
            System.out.println(wiimotes[0]);
            Wiimote wiimote = wiimotes[0];
            wiimote.setVirtualResolution(1024,768);
            wiimote.activateIRTRacking();
            PAppletExt.main( new String[]{"hemisphere.Main"} ,wiimote);
       } else {
            System.err.println("WARNING: WIIMOTE NOT CONNECTED");
             PAppletExt.main( new String[]{"hemisphere.Main"} ,null);
        }
    }
}
