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
public class TestMultiple {
    
    //static Wiimote mote=null;
    //static PApplet hemisphere;//  = new Main();
   //static PApplet spherical;// = new Spherical();
    /** Creates a new instance of TestMultiple */
    static Wiimote[] wiimotes = null;
    
       
    public static void main(String[] args) {
        //PApplet.main( new String[]{"--present","--display=2","hemisphere.Main"} );
        //PApplet.main( new String[]{"--present","--display=1","hemisphere.Main"} );
        wiimotes = WiiUseApiManager.getWiimotes(2, true);
        //Wiimote mote=null;
        System.out.println("NB DETECTED: " + wiimotes.length);
        if (wiimotes.length!= 0) {
            
            System.out.println(wiimotes[0]);
            Wiimote wiimote = wiimotes[0];
            wiimote.setVirtualResolution(1024,768);
            wiimote.activateIRTRacking();
            //  wiimote.activateMotionSensing();
            //wiimote.addWiiMoteEventListeners(this);

            System.out.println(wiimotes[1]);
            Wiimote wiimote2 = wiimotes[1];
            wiimote2.setVirtualResolution(1024,768);
            wiimote2.activateIRTRacking();
            
        PAppletExt.main( new String[]{"--location=0,0","--display=2","hemisphere.Main"} ,wiimote);
        //PAppletExt.main( new String[]{"--location=640,0","--display=1","hemisphere.Spherical"} ,wiimote2);
        PAppletExt.main( new String[]{"--location=640,0","--display=1","hemisphere.Main"} ,wiimote2);
       } else {
            System.err.println("WARNING: WIIMOTE NOT CONNECTED");
             PAppletExt.main( new String[]{"--location=0,0","--display=2","hemisphere.Main"} ,null);
                PAppletExt.main( new String[]{"--location=640,0","--display=1","hemisphere.Spherical"} ,null);
        }
        // PApplet hemisphere = new Main();
        //PApplet spherical = new Spherical();
        //hemisphere.main( new String[]{"--location=0,0","--display=2","hemisphere.Main"} );
        //spherical.main( new String[]{"--location=640,0","--display=1","hemisphere.Spherical"} );
        
    }
}
