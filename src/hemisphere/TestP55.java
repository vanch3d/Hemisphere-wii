/*
 * TestP55.java
 *
 * Created on 09 July 2008, 17:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hemisphere;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import processing.core.PApplet;


/**
 *
 * @author nicolas
 */
public class TestP55 extends Frame {
    
    private int w = 400, h = 422;
  
    
// constructor
    public TestP55() {
// call to superclass needs to come first in constructor
        super("Frame with Embedded PApplet Component");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
// set up frame (which will hold applet)
        setSize(dim.width, dim.height);
        setLayout(new BorderLayout());
        
        Panel processing = new Panel();
        processing.setLayout(new GridLayout(1,2));
        
        // Instantiate Applet object
        Main p55 = new Main(dim.width/2, dim.height);//new Spherical(dim.width/2, dim.height);
        Spherical p66 = new Spherical(dim.width/2, dim.height);
        p55.init();
        p66.init();
        
        processing.add(p55);
        processing.add(p66);
        
        Panel control = new Panel();
        control.setLayout(new FlowLayout());
        Checkbox checkbox1 = new Checkbox();
        Checkbox checkbox2 = new Checkbox();
        
        checkbox1.setLabel("Show angles");
        control.add(checkbox1);
        checkbox2.setLabel("Show circles");
        control.add(checkbox2);
        
        // add Applet component to frame
        add("Center",processing);
        add("South",control);
        
// won't allow frame to be resized
        setResizable(false);
        
// allow window and application to be closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
// Next comment taken directly from PApplet class:
/* "...ensures that the animation thread is started
and that other internal variables are properly set."*/

    }
    public static void main(String[] s){
        new TestP55().setVisible(true);
    }
}
