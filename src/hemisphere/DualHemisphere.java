/*
 * DualHemisphere.java
 *
 * Created on 09 July 2008, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hemisphere;

import java.awt.Dimension;
import java.awt.Toolkit;
import processing.core.PApplet;

/**
 *
 * @author nicolas
 */
public class DualHemisphere extends javax.swing.JFrame {
    
    private int w = 400, h = 422;
    PApplet hemisphere  =null;
    PApplet spherical = null;
  

    /** Creates a new instance of DualHemisphere */
    public DualHemisphere() {
        initComponents();
    }
    
     private void initComponents() 
     {
        //button2 = new java.awt.Button();

        getContentPane().setLayout(new java.awt.GridLayout(1, 2));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                setSize(dim.width, dim.height);
                
        setResizable(false);
        
        hemisphere = new Main(dim.width/2, dim.height);
        spherical = new P55(dim.width/2, dim.height);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(hemisphere);
        getContentPane().add(spherical);

        hemisphere.init();
        spherical.init();
        pack();
    }
     
      public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DualHemisphere().setVisible(true);
            }
        });
    }
}
