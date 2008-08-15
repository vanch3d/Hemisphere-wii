/*
 * Main.java
 *
 * Created on 04 June 2008, 10:52
 */

package hemisphere;

import com.processinghacks.arcball.ArcBallExt;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Vector;
import org.apache.commons.math.geometry.Vector3D;
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
public class Main extends PApplet implements WiimoteListener {

    private int w=-1, h=-1;

    ArcBallExt arcball;
    Sphere universe;
    SpotOn spot;
    SpotOn spot2;
    SpotOn spot3;
    Geodesic geodesic;
    Geodesic geodesic1;
    Geodesic geodesic2;
    PImage texture;
    PImage city;

    boolean showAngles = false;
    boolean showCircles = false;
    
    /** Creates a new instance of Main */
    float radius = 300;
    
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
        PApplet.main( new String[]{"hemisphere.Main"} );
    }
    
    /** Creates a new instance of of the program */
    public Main() {
        System.out.println("MAIN 1");
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
    public Main(int w,int h) {
          System.out.println("MAIN 2");
      
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
        
        System.out.println("WIIMOTE: " + iREvent.getWiimoteId());
        IRSource[] points = iREvent.getIRPoints();
        int nbPoints = points.length;
        //System.err.println("NB POINTS: " + nbPoints);
        spot2.setVisible(false);
        spot.setVisible(false);
        spot3.setVisible(false);
        if (nbPoints==0) return;
        Vector3D vec =  Projection.get3DFromXY(points[0].getRx(),points[0].getRy());
           // System.err.println("[0] -> " + points[0].getRx() + "," + points[0].getRy());
        if (vec!=null) {
            spot.setVector(vec.scalarMultiply(2*radius));
            spot.setVisible(true);
        }
        if (nbPoints>=2) {
            vec =  Projection.get3DFromXY(points[1].getRx(),points[1].getRy());
               // System.err.println("[1] -> " + points[1].getRx() + "," + points[1].getRy());
            if (vec!=null) {
                spot2.setVector(vec.scalarMultiply(2*radius));
                spot2.setVisible(true);
            }
        }
        if (nbPoints>=3) {
            vec =  Projection.get3DFromXY(points[2].getRx(),points[2].getRy());
              //  System.err.println("[2] -> " + points[2].getRx() + "," + points[2].getRy());
            if (vec!=null) {
                spot3.setVector(vec.scalarMultiply(2*radius));
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
         //  System.err.println("HEMISPHERE: " + statusEvent.toString());
     
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
    
    
    void drawCylinder(float topRadius, float bottomRadius, float tall, int sides) {
  float angle = 0;
  float angleIncrement = TWO_PI / sides;
  beginShape(QUAD_STRIP);
  texture(city);
  for (int i = 0; i < sides + 1; ++i) {
      float u = city.width / (sides + 1) * i;
    vertex(topRadius*cos(angle), 0, topRadius*sin(angle),u, 0);
    
    vertex(bottomRadius*cos(angle), tall, bottomRadius*sin(angle),u, city.height);
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

        Vector3D vec1 = Vector3D.crossProduct(spot1,spot2);
        Vector3D vec2 = Vector3D.crossProduct(spot1,spot3);
        
        f = (float)Vector3D.angle(vec1,vec2);
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
            if (spot1.getX()>=0)
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
        
        //texture = loadImage("earth_sphere.jpg");
          //texture = loadImage("earth_sphere2.png");
          texture = loadImage("earth_sphere3.png");
            city= loadImage("building2.jpg");

        
        if (this.h == -1 && this.w==-1)
        {
            size(screen.width/2,(int)( screen.height*2.f/3.f),P3D);
        }
        else
        {
            int titleBarHt = getBounds().y;
            size(w, h-titleBarHt,P3D);
        }
        
        //((PGraphics3D)g).triangle.setCulling(false);
        radius = Math.min(width, height)/2;
        //size(screen.width/2, screen.height/2,P3D);
        //radius = Math.min(screen.width/2, screen.height/2)/2;
        
        universe = new Sphere(g, 100, 100);
        universe.setScale(radius);
       universe.setTexture(texture,"WHOLE");
        
        Vector3D pos = new Vector3D(-0.570f, -0.58f, 0.57f);
        //Vector3D pos = new Vector3D(1, 1, 0);
        pos = pos.normalize();
        pos = pos.scalarMultiply(radius);
        spot=new SpotOn(g, 50, 50,pos);
        spot.setScale(20);
        
        pos = new Vector3D(-0.068f,  0.77f, 0.63f);
        //pos = new Vector3D(1,  1, 1);
        pos = pos.normalize();
        pos = pos.scalarMultiply(radius);
        spot2=new SpotOn(g, 50, 50, pos);
        spot2.setScale(radius/20);

        pos = new Vector3D(0.910f,  0.18f, 0.35f);
        //pos = new Vector3D(-1,  1,0);
        pos = pos.normalize();
        pos = pos.scalarMultiply(radius);
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
        
        arcball = new ArcBallExt(this);
        arcball.setActive(true);
        
        for( int i = 0; i < MaxPts; i++ )
            {
            
            coords.addElement( new vec2() );
            }
       Resample();  
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
            background(255);
            
            
            translate(width/2,height/2,-Math.min(width/2, height/2));
            //lights();
   pointLight(255, 255, 255, // Color
             0, -0, 450); // Position

  /// Blue directional light from the left
  directionalLight(255, 255, 255, // Color
                   1, 0, 0); // The x-, y-, z-axis direction
  /// Blue directional light from the left
  directionalLight(255, 255, 255, // Color
                   -1, 0, 0); // The x-, y-, z-axis direction

 /* // Yellow spotlight from the front
  spotLight(255f, 255f, 109f, // Color
            0f, 40f, 800f, // Position
            0f, -0.5f, -0.5f, // Direction
            PI / 2f, 2f); // Angle, concentration   */
           

  
                       // Orange point light on the right
            // Draw the 3D-axis
            pushMatrix();
                noStroke();
                fill(255,0,0);
                drawCylinder(0,0,0,2,radius+50,0,0,radians(-90));
                translate(radius+50,0,0);
                rotateZ(radians(-90));
                drawCone(10, 0, 40, 64);
            popMatrix();
            
            pushMatrix();
                fill(0,255,0);
                drawCylinder(0,0,0,2,radius+50,0,radians(-90),0);
                translate(0,radius+50,0);
                drawCone(10, 0, 40, 64);
            popMatrix();
        
            pushMatrix();
                fill(0,0,255);
                drawCylinder(0,0,0,2,radius+50,radians(90),0,0);
                rotateX(radians(90));
                translate(0,radius+50,0);
                drawCone(10, 0, 40, 64);
            popMatrix();
            
            // Draw the universe
            pushMatrix();
                noStroke();
                //stroke(0,0,0);
                fill(255,231,125,20);
                //noFill();
                rotateZ(radians(180));
                universe.drawPart(0,universe.phiSteps(),0,universe.thetaSteps()/2);
                noFill();
                stroke(255,231,125,20);
                //universe.drawPart(0,universe.phiSteps(),universe.thetaSteps()/2,universe.thetaSteps());
            popMatrix();
      // Draw the first spot
        if (spot.isVisible()) 
        {
            noStroke();
            fill(125,149,255);
            pushMatrix();
            translate((float)spot.getVector().getX(),(float)spot.getVector().getY(),(float)spot.getVector().getZ());
            spot.draw();
            if (spot2.isVisible() && spot3.isVisible() && showAngles)
            {
                float f1= drawAngle(spot.getVector(),spot2.getVector(),spot3.getVector(),"a=");
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
            translate((float)spot2.getVector().getX(),(float)spot2.getVector().getY(),(float)spot2.getVector().getZ());
            spot2.draw();
            if (spot.isVisible() && spot3.isVisible() && showAngles)
             {
                float f2= drawAngle(spot2.getVector(),spot.getVector(),spot3.getVector(),"b=");
             }
           popMatrix();
        }

        // Draw the third spot
        if (spot3.isVisible()) {
            pushMatrix();
            translate((float)spot3.getVector().getX(),(float)spot3.getVector().getY(),(float)spot3.getVector().getZ());
            noStroke();
            fill(125,149,255);
            //fill(125,255,214);
            
            spot3.draw();
            if (spot.isVisible() && spot2.isVisible() && showAngles)
            {
                float f3= drawAngle(spot3.getVector(),spot.getVector(),spot2.getVector(),"c=");
            }
            popMatrix();
            }
        // Draw the geodesics
        if (showCircles)
        {
           // pushMatrix();
            geodesic.draw();
           geodesic1.draw();
            geodesic2.draw();
          //  popMatrix();
        }
            
              // Draw New York
        pushMatrix();
        fill(255,255,0);
        Vector3D posNY = new Vector3D(-0.570f, 0.58f, 0.57f);
        posNY=posNY.normalize();
        posNY=posNY.scalarMultiply(radius);

        
          SpotOn spotNY=new SpotOn(g, 50, 50, posNY);
         spotNY.setScale(radius/20);
        translate((float)spotNY.getVector().getX(),(float)spotNY.getVector().getY(),(float)spotNY.getVector().getZ());
          //  spotNY.draw();
              popMatrix();
         pushMatrix();
    //translate(posNY.x,posNY.y,posNY.z);
            
        float vx = (float)posNY.getX();
        float vy = (float)posNY.getY();
        float vz = (float)posNY.getZ();
        float v = sqrt( vx*vx + vy*vy + vz*vz );	// cylinder length
	
     // rotation vector, z x r  
	float rx = -vy*vz;
	float ry = +vx*vz;
	float ax = 0.0f;
        Vector3D axis= new Vector3D(rx,ry,0f);
        axis=axis.normalize();
        float zero = 1.0e-3f;
	if (abs(vz) < zero)
	{
		ax = acos( vx/v );	// rotation angle in x-y plane
		if ( vx <= 0.0 ) ax = -ax;
	}
	else
	{
		ax = acos( vz/v );	// rotation angle
		if ( vz <= 0.0 ) ax = -ax;
	}

        translate( vx,vy,vz );	// translate to point 1
	
		if (abs(vz) < zero)
		{
			rotate(PI/2, 0f, 1f, 0.0f);			// Rotate & align with x axis
			rotate(ax, -1.0f, 0.0f, 0.0f);		// Rotate to point 2 in x-y plane
		}
		else
		{
			//rotate(ax, rx, ry, 0.0f);			// Rotate about rotation vector
			rotate(ax, (float)axis.getX(),(float)axis.getY(),(float)axis.getZ());			// Rotate about rotation vector
		}
rotate(PI/2, 1f, 0f, 0.0f);
              drawCylinder(10,10,radius/20,20);
              translate(radius/40,0,0 );
              drawCylinder(10,10,radius/30,20);
              //translate(-radius/30,0,radius/40 );
              //drawCylinder(10,10,radius/15,20);
              //drawCylinder(0,0,0, 10, 100,0,0,0);
        popMatrix();
            
        }
        
        
    public void draw4() 
    {
        // compute the three angles
        float f1=0.f,f2=0.f,f3=0.f;
             
        background(255);
        lights();
      
        pushMatrix();        
        // Center the display
        translate(width/2,height/2,-Math.min(width/2, height/2));
  
        // Draw New York
        pushMatrix();
        fill(255,255,0);
        Vector3D posNY = new Vector3D(0.570f, 0.58f, 0.57f);
        posNY=posNY.normalize();
        posNY=posNY.scalarMultiply(radius);

        
          SpotOn spotNY=new SpotOn(g, 50, 50, posNY);
         spotNY.setScale(radius/20);
        translate((float)spotNY.getVector().getX(),(float)spotNY.getVector().getY(),(float)spotNY.getVector().getZ());
          //  spotNY.draw();
              popMatrix();
         pushMatrix();
    //translate(posNY.x,posNY.y,posNY.z);
            
        float vx = (float)posNY.getX();
        float vy = (float)posNY.getY();
        float vz = (float)posNY.getZ();
        float v = sqrt( vx*vx + vy*vy + vz*vz );	// cylinder length
	
     // rotation vector, z x r  
	float rx = -vy*vz;
	float ry = +vx*vz;
	float ax = 0.0f;
        Vector3D axis= new Vector3D(rx,ry,0f);
        axis=axis.normalize();
        float zero = 1.0e-3f;
	if (abs(vz) < zero)
	{
		ax = acos( vx/v );	// rotation angle in x-y plane
		if ( vx <= 0.0 ) ax = -ax;
	}
	else
	{
		ax = acos( vz/v );	// rotation angle
		if ( vz <= 0.0 ) ax = -ax;
	}

        translate( vx,vy,vz );	// translate to point 1
	
		if (abs(vz) < zero)
		{
			rotate(PI/2, 0f, 1f, 0.0f);			// Rotate & align with x axis
			rotate(ax, -1.0f, 0.0f, 0.0f);		// Rotate to point 2 in x-y plane
		}
		else
		{
			//rotate(ax, rx, ry, 0.0f);			// Rotate about rotation vector
			rotate(ax, (float)axis.getX(),(float)axis.getY(),(float)axis.getZ());			// Rotate about rotation vector
		}
rotate(PI/2, 1f, 0f, 0.0f);
              drawCylinder(10,10,radius/20,20);
              translate(radius/40,0,0 );
              drawCylinder(10,10,radius/30,20);
              //translate(-radius/30,0,radius/40 );
              //drawCylinder(10,10,radius/15,20);
              //drawCylinder(0,0,0, 10, 100,0,0,0);
        popMatrix();
       
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
        //        universe = new Sphere(g, 50, 150);
        universe.drawPart(0,universe.phiSteps(),0,universe.thetaSteps()/2);
       noFill();
       stroke(255,231,125,20);
       universe.drawPart(0,universe.phiSteps(),universe.thetaSteps()/2,universe.thetaSteps());
               
      // Draw the first spot
        if (spot.isVisible()) 
        {
            noStroke();
            fill(125,149,255);
            pushMatrix();
            translate((float)spot.getVector().getX(),(float)spot.getVector().getY(),(float)spot.getVector().getZ());
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
            translate((float)spot2.getVector().getX(),(float)spot2.getVector().getY(),(float)spot2.getVector().getZ());
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
            translate((float)spot3.getVector().getX(),(float)spot3.getVector().getY(),(float)spot3.getVector().getZ());
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
           // pushMatrix();
            geodesic.draw();
           geodesic1.draw();
            geodesic2.draw();
          //  popMatrix();
        }
         
        // Compute and draw the sum of the angles
        if (spot.isVisible() && spot3.isVisible() && spot2.isVisible() && showAngles)
        {
            // Get the triangle's centroid
            Vector3D c1 = spot2.getVector();
            c1 = c1.scalarMultiply(1/3.f);
            Vector3D c2 = spot.getVector();
            c2=c2.scalarMultiply(1/3.f);
            Vector3D c3 = spot3.getVector();
            c3=c3.scalarMultiply(1/3.f);

            Vector3D centroid = c1;
            centroid=centroid.add(c2);
            centroid=centroid.add(c3);
            centroid=centroid.normalize();
            centroid=centroid.scalarMultiply(radius);
            
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
                translate((float)centroid.getX(),(float)centroid.getY(),(float)centroid.getZ());
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
        /*
        float[] aa = arcball.getNow();
        float[] bb = arcball.getCenter();
        translate(bb[0], bb[1],bb[2]); 
        rotate(aa[0], -aa[1], -aa[2], -aa[3]);
        //translate(-width/2,-height/2,Math.min(width/2, height/2));              
        translate(0,0,2*Math.min(width/2, height/2));              
        noLights();
        //scale(0.5f);
        text("a+b+c= 5200",0,20,0);*/
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
