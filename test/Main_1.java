/*
 * Main.java
 *
 * Created on 04 June 2008, 10:52
 */

import com.processinghacks.arcball.ArcBallExt;
import hemisphere.*;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Vector;
import processing.core.*;
import surface.*;
import surface.Geodesic;
import surface.Hemisphere;
import utils.Projection;
import surface.SpotOn;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.values.IRSource;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.physicalevents.*;
import wiiusej.wiiusejevents.wiiuseapievents.*;

/**
 * The main entry point of the henisphere project, embedding the PROCESSING code.
 *
 * @author Nicolas Van Labeke
 */
public class Main_1 extends PApplet implements WiimoteListener {

    private int w=-1, h=-1;

    ArcBallExt arcball;
    Hemisphere universe;
    SpotOn spot;
    SpotOn spot2;
    SpotOn spot3;
    Geodesic geodesic;
    Geodesic geodesic1;
    Geodesic geodesic2;
    
    PGraphics3D right;

   vec2i    p = new vec2i(),
             q = new vec2i(),
             center = new vec2i();

       
    float rotation;
    boolean showAngles = false;
    boolean showCircles = false;
    
    /** Creates a new instance of Main */
    float radius = 50;
    
    /** References to the detected WiiMotes */
    Wiimote[] wiimotes = null;
    
    
  int              MaxPts = 400,
             npts   = 20;
   Vector    coords = new Vector();

     Random   rng = new Random( 123456 );
   
     
    ////////////////////////////////////////////////////////////////////////////////////
    /// Program internal management
    ////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Execution point for the program
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PApplet.main( new String[]{"hemisphere.Main_1"} );
    }
    
    /** Creates a new instance of of the program */
    public Main_1() {
        
    /*    wiimotes = WiiUseApiManager.getWiimotes(1, true);
        if (wiimotes.length!= 0) {
            
            System.out.println(wiimotes[0]);
            Wiimote wiimote = wiimotes[0];
            wiimote.setVirtualResolution(1024,768);
            wiimote.activateIRTRacking();
            //  wiimote.activateMotionSensing();
            wiimote.addWiiMoteEventListeners(this);
        } else {
            System.err.println("WARNING: WIIMOTE NOT CONNECTED");
        }*/
    }

        /** Creates a new instance of of the program */
    public Main_1(int w,int h) {
        
        this.w = w;
        this.h = h;
        
       /* wiimotes = WiiUseApiManager.getWiimotes(1, true);
        if (wiimotes.length!= 0) {
            
            System.out.println(wiimotes[0]);
            Wiimote wiimote = wiimotes[0];
            wiimote.setVirtualResolution(1024,768);
            wiimote.activateIRTRacking();
            //  wiimote.activateMotionSensing();
            wiimote.addWiiMoteEventListeners(this);
        } else {
            System.err.println("WARNING: WIIMOTE NOT CONNECTED");
        }*/
    }

    ////////////////////////////////////////////////////////////////////////////////////
    /// Wiimote management
    ////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Wiimote reconnection
     * @deprecated Do not use! Problems with the Bluetooth stack means unstable reconnection.
     * If the Wiimote is not detected while executing the program, shut it down and start again.
     */
    private void reconnect() {
        WiiUseApiManager.shutdown();
        wiimotes = WiiUseApiManager.getWiimotes(1, true);
    }
    
    
     /**
     * Listener for the wiimote's IR events
     * @param iREvent A reference to the IR event emitted by the wiimote
     */
   public void onIrEvent(IREvent iREvent) {
        
        IRSource[] points = iREvent.getIRPoints();
        int nbPoints = points.length;
        System.err.println("NB POINTS: " + nbPoints);
        spot2.setVisible(false);
        spot.setVisible(false);
        spot3.setVisible(false);
        if (nbPoints==0) return;
        Vector3D vec =  Projection.get3DFromXY(points[0].getRx(),points[0].getRy());
            System.err.println("[0] -> " + points[0].getRx() + "," + points[0].getRy());
        if (vec!=null) {
            spot.setVector(Vector3D.mult(vec,2*radius));
            spot.setVisible(true);
        }
        if (nbPoints>=2) {
            vec =  Projection.get3DFromXY(points[1].getRx(),points[1].getRy());
                System.err.println("[1] -> " + points[1].getRx() + "," + points[1].getRy());
            if (vec!=null) {
                spot2.setVector(Vector3D.mult(vec,2*radius));
                spot2.setVisible(true);
            }
        }
        if (nbPoints>=3) {
            vec =  Projection.get3DFromXY(points[2].getRx(),points[2].getRy());
                System.err.println("[2] -> " + points[2].getRx() + "," + points[2].getRy());
            if (vec!=null) {
                spot3.setVector(Vector3D.mult(vec,2*radius));
                spot3.setVisible(true);
            }
        }
    }
    
    public void onButtonsEvent(WiimoteButtonsEvent wiimoteButtonsEvent) 
    {
    }
    public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {}
    public void onExpansionEvent(ExpansionEvent expansionEvent) {}
    public void onStatusEvent(StatusEvent statusEvent)
    {
           System.err.println("HEMISPHERE: " + statusEvent.toString());
     
    }
    public void onDisconnectionEvent(DisconnectionEvent disconnectionEvent) {}
    public void onNunchukInsertedEvent(NunchukInsertedEvent nunchukInsertedEvent) {}
    public void onNunchukRemovedEvent(NunchukRemovedEvent nunchukRemovedEvent) {}
    
    ////////////////////////////////////////////////////////////////////////////////////
    /// Processing instructions
    ////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * 
     * @param x 
     * @param y 
     * @param z 
     * @param xr 
     * @param yr 
     * @param rx 
     * @param ry 
     * @param rz 
     * @todo Should try to do it with the Surface package.
     */
    private void drawCylinder(float x,float y,float z, float xr,float yr,float rx, float ry, float rz) {
        float num=36;
        float degree=radians(360/num);
        
        pushMatrix();
        translate(x,y,z);
        rotateX(rx);
        rotateY(ry);
        rotateZ(rz);
        
        beginShape(QUAD_STRIP);
        for(int i=0; i<num+1; i++) {
            float cosval=cos(degree*i);
            float sinval=sin(degree*i);
            vertex(cosval*xr,-0,sinval*xr);
            vertex(cosval*xr, yr,sinval*xr);
        }
        endShape();
        popMatrix();
    }
    
    
    /**
     * Processing method for drawing a cone/cylinder
     * @todo Should try to do it with the Surface package.
     * @param topRadius the radius at the top of the cylinder (0 for a cone)
     * @param bottomRadius the radius at the bottom of the cylinder (0 for a cone)
     * @param tall height of the cylinder
     * @param sides number of sides in the mesh
     */
    private void drawCone(float topRadius, float bottomRadius, float tall, int sides) {
        float angle = 0;
        float angleIncrement = TWO_PI / sides;
        beginShape(QUAD_STRIP);
        for (int i = 0; i < sides + 1; ++i) {
            vertex(topRadius*cos(angle), 0, topRadius*sin(angle));
            vertex(bottomRadius*cos(angle), tall, bottomRadius*sin(angle));
            angle += angleIncrement;
        }
        endShape();
        
        // If it is not a cone, draw the circular top cap
        if (topRadius != 0) {
            angle = 0;
            beginShape(TRIANGLE_FAN);
            
            // Center point
            vertex(0, 0, 0);
            for (int i = 0; i < sides + 1; i++) {
                vertex(topRadius * cos(angle), 0, topRadius * sin(angle));
                angle += angleIncrement;
            }
            endShape();
        }
        
        // If it is not a cone, draw the circular bottom cap
        if (bottomRadius != 0) {
            angle = 0;
            beginShape(TRIANGLE_FAN);
            
            // Center point
            vertex(0, tall, 0);
            for (int i = 0; i < sides + 1; i++) {
                vertex(bottomRadius * cos(angle), tall, bottomRadius * sin(angle));
                angle += angleIncrement;
            }
            endShape();
        }
    }
    
        /**
     * Processing method for drawing the value of an angle 
     * NOTE: the text is suppose to be displayed near the SpotOn object itself
     * @param spot1 the coordinate of the center of the triangle
     * @param spot2 the coordinates of the second point
     * @param spot3 the coordinates of the third point
     * @param label the label to start the text with.
     * @return the value of the angle
     */
    public float drawAngle(Vector3D spot1,Vector3D spot2,Vector3D spot3,String label)
    {
        float f = 0.0f;

        Vector3D vec1 = spot1.cross(spot2);
        Vector3D vec2 = spot1.cross(spot3);
        
        f = Vector3D.angleBetween(vec1,vec2);
        Float ff = new Float(f);
        if (!ff.isNaN() && !ff.isInfinite())
        {
            pushMatrix();
            fill(0,0,255);
            // Trick to maintain the text parallel to the projection plan (use the arcball's rotation).
            float[] aa = arcball.getNow();
            rotate(aa[0], -aa[1], -aa[2], -aa[3]);
            
            BigDecimal bd1 = new BigDecimal(f/PI*180);
            bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (spot1.x>=0)
                    text(label + " " + bd1.toString(),5,0,80);
                else
                    text(label + " " + bd1.toString(),-150,0,80);
           // text(label + " " + bd1.toString(),50,50,10);
            popMatrix();
        }
        return f;
    }
    
    
    
    public void setup() {
        PFont font = loadFont("Ziggurat.vlw"); 
        textFont(font); 
        
        if (this.h == -1 && this.w==-1)
        {
            size(screen.width, screen.height,P3D);
        }
        else
        {
            int titleBarHt = getBounds().y;
            size(w, h-titleBarHt,P3D);
        }
        
        //((PGraphics3D)g).triangle.setCulling(false);
        radius = Math.min(width, height)/4;
        //size(screen.width/2, screen.height/2,P3D);
        //radius = Math.min(screen.width/2, screen.height/2)/2;
        
        universe = new Hemisphere(g, 50, 150);
        universe.setScale(radius);
        
        Vector3D pos = new Vector3D(0,0,radius);
        pos.normalize();
        pos.mult(radius);
        spot=new SpotOn(g, 50, 50,pos);
        spot.setScale(20);
        
        pos = new Vector3D(0,radius,0);
        pos.normalize();
        pos.mult(radius);
        spot2=new SpotOn(g, 50, 50, pos);
        spot2.setScale(radius/20);

        pos = new Vector3D(-radius,-radius,0);
        pos.normalize();
        pos.mult(radius);
        spot3=new SpotOn(g, 50, 50, pos);
        spot3.setScale(20);
        
        geodesic = new Geodesic(g, 100,10,spot,spot2);
        geodesic.setRadius(radius,3);
        geodesic.setScale(1);
        geodesic1 = new Geodesic(g, 100,10,spot,spot3);
        geodesic1.setRadius(radius,3);
        geodesic1.setScale(1);
        geodesic2 = new Geodesic(g, 100,10,spot2,spot3);
        geodesic2.setRadius(radius,3);
        geodesic2.setScale(1);
        
        arcball = new ArcBallExt(g.width/4.0f,g.height/2.0f,0,PApplet.min(g.width/4.0f,g.height/2.0f),this);
        arcball.setActive(true);
        
        for( int i = 0; i < MaxPts; i++ )
            {
            
            coords.addElement( new vec2() );
            }
       Resample();  
       
       right=new PGraphics3D(width/2,height,null);
  right.defaults();
  right.ambientLight(64,64,64);
  right.pointLight(128,128,128,0,20,100);
  right.noStroke();

    }
    
 public void Resample()
        {
        int k = 0;
        for( int i = 0; i < npts; i++ )
        for( int j = 0; j < npts; j++ )
            {
            float x = rng.nextFloat();
            float y = rng.nextFloat();
            ( (vec2)coords.elementAt(k++) ).set( x, y );
            }        
        }
    
    public void draw() 
    {
        // compute the three angles
        float f1=0.f,f2=0.f,f3=0.f;
         
        background(255);
        lights();
      
      
      pushMatrix();        
        // Center the display
        translate(width/4,height/2,0);
      scale(0.9f);
       
       
        // Draw the 3D-axis
        noStroke();
        fill(255,0,0);
        drawCylinder(0,0,0,2,radius+50,0,0,radians(-90));
        pushMatrix();
        translate(radius+50,0,0);
        rotateZ(radians(-90));
        drawCone(10, 0, 40, 64);
        popMatrix();

        fill(0,255,0);
        drawCylinder(0,0,0,2,radius+50,0,radians(-90),0);
        pushMatrix();
        translate(0,radius+50,0);
        drawCone(10, 0, 40, 64);
        popMatrix();
        
        fill(0,0,255);
        drawCylinder(0,0,0,2,radius+50,radians(90),0,0);
        pushMatrix();
        rotateX(radians(90));
        translate(0,radius+50,0);
        drawCone(10, 0, 40, 64);
        popMatrix();
        
       /// Draw the universe
        noStroke();
        fill(255,231,125);
        universe.draw();
               
      // Draw the first spot
        if (spot.isVisible()) 
        {
            noStroke();
            fill(125,149,255);
            pushMatrix();
            translate(spot.getVector().x,spot.getVector().y,spot.getVector().z);
            spot.draw();
            if (spot2.isVisible() && spot3.isVisible() && showAngles)
            {
                f1= drawAngle(spot.getVector(),spot2.getVector(),spot3.getVector(),"a=");
            }
            popMatrix();
        }
        
        // Draw the second spot
        if (spot2.isVisible()) 
        {
            noStroke();
            fill(125,149,255);
            //fill(255,125,214);
            pushMatrix();
            translate(spot2.getVector().x,spot2.getVector().y,spot2.getVector().z);
            spot2.draw();
            if (spot.isVisible() && spot3.isVisible() && showAngles)
             {
                f2= drawAngle(spot2.getVector(),spot.getVector(),spot3.getVector(),"b=");
             }
           popMatrix();
        }

        // Draw the third spot
        if (spot3.isVisible()) {
            pushMatrix();
            translate(spot3.getVector().x,spot3.getVector().y,spot3.getVector().z);
            noStroke();
            fill(125,149,255);
            //fill(125,255,214);
            
            spot3.draw();
            if (spot.isVisible() && spot2.isVisible() && showAngles)
            {
                f3= drawAngle(spot3.getVector(),spot.getVector(),spot2.getVector(),"c=");
            }
            popMatrix();
        }
        
        // Draw the geodesics
        if (showCircles)
        {
            pushMatrix();
            geodesic.draw();
            geodesic1.draw();
            geodesic2.draw();
            popMatrix();
        }
         
        // Compute and draw the sum of the angles
        if (spot.isVisible() && spot3.isVisible() && spot2.isVisible() && showAngles)
        {
            // Get the triangle's centroid
            Vector3D c1 = spot2.getVector().copy();
            c1.mult(1/3.f);
            Vector3D c2 = spot.getVector().copy();
            c2.mult(1/3.f);
            Vector3D c3 = spot3.getVector().copy();
            c3.mult(1/3.f);

            Vector3D centroid = c1.copy();
            centroid.add(c2);
            centroid.add(c3);
            centroid.normalize();
            centroid.mult(radius);
            
            //Vector3D v = Vector3D.sub(spot2.getVector(),spot.getVector());
            //Vector3D w = Vector3D.sub(spot3.getVector(),spot.getVector());
            //Vector3D norm = v.cross(w);
            //norm.normalize();
            //norm.mult(radius);
            //float dis = norm.dot(new Vector3D(0,0,1));
            //if (dis<0)
            //    norm.mult(-1);
            
            Float ff = new Float(f1+f2+f3);
            if (!ff.isNaN() && !ff.isInfinite())
            {
                BigDecimal bd1 = new BigDecimal(ff.floatValue()/PI*180);
                bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);

                pushMatrix();
                translate(centroid.x,centroid.y,centroid.z);
                fill(255,125,149);
                SpotOn spotg=new SpotOn(g, 50, 50,centroid);
                spotg.setScale(20);
                spotg.draw();
        
                float[] aa = arcball.getNow();
                rotate(aa[0], -aa[1], -aa[2], -aa[3]);
                fill(255,0,0);
                text("a+b+c= " + bd1.toString(),0,0,80);
                popMatrix();
            }
        }
        
        popMatrix();
        
        rotation+=0.01;
     
        
        float[] aa = arcball.getNow();
        float[] bb = arcball.getCenter();
        translate(bb[0], bb[1],bb[2]); 
        rotate(aa[0], -aa[1], -aa[2], -aa[3]);
        //translate(-width/2,-height/2,Math.min(width/2, height/2));              
        //translate(0,0,Math.min(width/2, height/2));              
        translate(-width/4+width/2,-height/2,00);
        center.x =width/2;
        center.y = height / 2;
        radius = width/8;
        noLights();
        //scale(0.5f);
        
       // line(0,0,0,height);
        text("a+b+c="+center.x, center.x, center.y);
        drawScene(right,10);
        image(right,0,0);
        
         
    }
    

        
      private void drawLine(PGraphics3D target, float x1, float y1, float x2, float y2 )
        {
        target.line( 
            center.x + (int)( x1 * radius ),
            center.y + (int)( y1 * radius ),
            center.x + (int)( x2 * radius ),
            center.y + (int)( y2 * radius )
            );
        }
      
     private void drawXYAxes(PGraphics3D target)
        {
            target.strokeWeight(2);
           target. stroke(255,0,0,255);
            target.fill(255,0,0,255);
           drawLine(target,0,0,1.1f,0);
           target.triangle(
                   center.x + (int)(1.2 * radius ),center.y,
                   center.x + (int)(1.1 * radius ),center.y + (int)(0.025 * radius ),
                    center.x + (int)(1.1 * radius ),center.y - (int)(0.025 * radius ));
            target.stroke(0,255,0,255);
           target. fill(0,255,0,255);
           drawLine(target,0,0,0,1.1f);
            target.triangle(
                   center.x ,center.y + (int)(1.2 * radius ),
                   center.x + (int)(0.025 * radius ),center.y + (int)(1.1 * radius ),
                    center.x - (int)(0.025 * radius ),center.y + (int)(1.1 * radius ));
           strokeWeight(1);
        }


    void drawScene(PGraphics3D target, int eyePosition)
{
  target.loadPixels();
  target.background(0);
  target.fill(255);
  
  // Left Eye
  target.camera(eyePosition,0,100,0,0,0,0,-1,0); //using Camera gives us much more control over the eye position.
  //target.rotateX(rotation);
  //target.rotateY(rotation/2.3f);
  //target.box(30);
  //target.translate(0,0,30);
  //target.box(10);
  target. stroke(255,0,0,255);
  target.line(0,0,radius,0);
  target. stroke(0,255,0,255);
  target.line(0,0,0,radius);
  target.ellipse(0,0,radius/10,radius/10);
  target.updatePixels();
   //drawXYAxes(target);
}

    
    
    public void keyPressed() {
        if (key == CODED) {
        } 
        else if (key == 'a' || key == 'A')
        {
            // Switch the visualisation of angles on/off
            showAngles = !showAngles;
        }
        else if (key == 'c' || key == 'C')
        {
            // Switch the visualisation of angles on/off
            showCircles = !showCircles;
        }
        else if (key == ' ') {
            //arcball.switchActive();
            //spot2.setVisible(!spot2.isVisible());
        }

    }
    
}
