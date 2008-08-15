/*
 * P55.java
 *
 * Created on 09 July 2008, 17:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hemisphere;

import processing.core.*;
import java.awt.*;

public class P55 extends PApplet {
    private int w, h;
    private int xc, yc;
    private float rx, ry, rw, rh;
    private float ang, radius;
    private float alph = 50.0f;
    private float spacer = 1.0f;
    
    public P55(int w, int h){
        this.w = w;
        this.h = h;
    }
    public void setup() {
/* account for height of frame title bar,
when sizing applet. getBounds() returns a Java
Rectangle object that has 4 public properties:
(x, y, width and height) */
        int titleBarHt = getBounds().y;
        size(w, h-titleBarHt);
        background(255);
    frameRate(300);
        noStroke();
        smooth();
        xc = width/2;
        yc = height/2;
    }
    public void draw(){
        if (ry < height){
            fill(50, alph);
            rx = xc + cos(ang)*radius;
            ry = yc + sin(ang)*radius;
            rw = cos(ang*spacer)*(radius/8.0f);
            rh = sin(ang*spacer)*(radius/8.0f);
            ellipse(rx, ry, rw, rh);
            ang += .075f;
            radius +=.08f;
            alph +=.001f;
            spacer +=.00001f;
        }
    }
}