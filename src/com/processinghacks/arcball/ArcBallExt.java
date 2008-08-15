/*
 * ArcBallExt.java
 *
 * Created on 04 June 2008, 10:57
 */

package com.processinghacks.arcball;

import processing.core.PApplet;
import java.awt.event.MouseEvent;

/**
 * Extension of the arcball package to switch the interface on/off 
 *
 * @author Nicolas Van Labeke
 */
public class ArcBallExt extends ArcBall  {
    
   private boolean isActive = false;
  
  public ArcBallExt(PApplet parent) {
    super(parent);
  }
  
   public ArcBallExt(float center_x, float center_y, float center_z, float radius, PApplet parent) {
       super( center_x, center_y, center_z, radius,parent);
   }
  
  public void setActive(boolean act)
  {
    this.isActive = act;
  }
  
  public void switchActive()
  {
    this.isActive = !this.isActive;
  }
  
   public void mouseEvent(MouseEvent event) {
    int id = event.getID();
    if (this.isActive)
    {
      super.mouseEvent(event);
    }
  }   
   
   public float[] getNow()
   {
       float[] aa = q_now.getValue();

       return aa;
   }
   
    public float[] getCenter()
   {
       float[] aa = new float[3];
       aa[0] = center_x;
       aa[1] = center_y;
       aa[2] = center_z;
       

       return aa;
   }
           
           
}
