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
public class Spherical extends PApplet implements WiimoteListener {
    
    private int w, h;
    
    /** References to the detected WiiMotes */
    Wiimote[] wiimotes = null;

    boolean showAngles = false;
    boolean showCircles = false;
    
        int      Tol    = 6,
             MaxPts = 400,
             npts   = 20,
             picked = 0,
             border,
             radius,
             diameter;

    boolean  show_pts    = false,
             do_uniform  = false,
             do_strat    = true,
             do_sphere   = false;

    Vector   points = new Vector(),
             coords = new Vector();

    vec2     r = new vec2();

    vec2i    p = new vec2i(),
             q = new vec2i(),
             center = new vec2i();
    
    unit3    A = new unit3( -0.570, -0.58, 0.57 ),
             B = new unit3( -0.068,  0.77, 0.63 ),
             C = new unit3(  0.910,  0.18, 0.35 ),
             P = new unit3(),
             M = new unit3(),
             Q = new unit3(),
             u = new unit3();

    Random   rng = new Random( 123456 );
    spherical_trigonometry   S   = new spherical_trigonometry( A, B, C );

    ////////////////////////////////////////////////////////////////////////////////////
    /// Program internal management
    ////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Execution point for the program
     * @param args the command line arguments
     */
    //public static void main(String[] args) {
    //    PApplet.main( new String[]{"hemisphere.Spherical"} );
   // }
    
    /** Creates a new instance of of the program */
    public Spherical(int w,int h) {
        
        this.w =w;
        this.h=h;
        
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

    public Spherical() {
        
        this.w =-1;
        this.h=-1;
        
     /*   wiimotes = WiiUseApiManager.getWiimotes(1, true);
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
        
        if (nbPoints==0) return;
        Vector3D vec =  Projection.get3DFromXY(points[0].getRx(),points[0].getRy());
            System.err.println("[0] -> " + points[0].getRx() + "," + points[0].getRy());
        if (vec!=null) {
                Vector3D pos = vec.scalarMultiply(2*radius);
                A=new unit3(pos.getX(),pos.getY(),pos.getZ());
        }
        if (nbPoints>=2) {
            vec =  Projection.get3DFromXY(points[1].getRx(),points[1].getRy());
                System.err.println("[1] -> " + points[1].getRx() + "," + points[1].getRy());
            if (vec!=null) {
                Vector3D pos = vec.scalarMultiply(2*radius);
                B=new unit3(pos.getX(),pos.getY(),pos.getZ());
            }
        }
        if (nbPoints>=3) {
            vec =  Projection.get3DFromXY(points[2].getRx(),points[2].getRy());
                System.err.println("[2] -> " + points[2].getRx() + "," + points[2].getRy());
            if (vec!=null) {
                  Vector3D pos = vec.scalarMultiply(2*radius);
                C=new unit3(pos.getX(),pos.getY(),pos.getZ());
           }
        }
            S.set(A,B,C);
   
   }
    
    public void onButtonsEvent(WiimoteButtonsEvent wiimoteButtonsEvent) {}
    public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {}
    public void onExpansionEvent(ExpansionEvent expansionEvent) {}
    public void onStatusEvent(StatusEvent statusEvent) {}
    public void onDisconnectionEvent(DisconnectionEvent disconnectionEvent) {}
    public void onNunchukInsertedEvent(NunchukInsertedEvent nunchukInsertedEvent) {}
    public void onNunchukRemovedEvent(NunchukRemovedEvent nunchukRemovedEvent) {}
    
    ////////////////////////////////////////////////////////////////////////////////////
    /// Processing instructions
    ////////////////////////////////////////////////////////////////////////////////////

    public void setup() {
       // PFont font = loadFont("Ziggurat.vlw"); 
       // textFont(font); 
        
        if (this.h == -1 && this.w==-1)
        {
            size(screen.width/2, (int)( screen.height*2.f/3.f));
        }
        else
        {
            int titleBarHt = getBounds().y;
            size(w, h-titleBarHt);
        }
        
        
       //size(screen.width/2-10, screen.height);
        
        border   = 80;
        center.x = width  / 2;
        center.y = height / 2;
        radius   = Math.min( width, height ) / 2 - border;
        diameter = 2 * radius;
        
        
        for( int i = 0; i < MaxPts; i++ )
            {
            coords.addElement( new vec2() );
            }
       Resample();        
    /*    Vector3D pos = new Vector3D(0,0,radius);
        pos.normalize();
        pos.mult(radius);
        A=new unit3(pos.x,pos.y,pos.z);
        
        pos = new Vector3D(0,radius,0);
        pos.normalize();
        pos.mult(radius);
        B=new unit3(pos.x,pos.y,pos.z);

        pos = new Vector3D(-radius,-radius,0);
        pos.normalize();
        pos.mult(radius);
        C=new unit3(pos.x,pos.y,pos.z);
        
        S.set(A,B,C);*/
 

    }
    
    
      private void drawPoints()
        {
        if( !show_pts ) return;
        stroke(0,255,0);
        strokeWeight(1);
        int k = 0;
	int np = coords.size();
        float si = (float)( 1.0 / npts );
        float sj = (float)( 1.0 / npts );
        for( int i = 0; i < npts; i++ )
        for( int j = 0; j < npts; j++ )
            {
            r = (vec2)coords.elementAt( k++ );
                {
                u.set( S.Sample( ( i + r.x ) * si, ( j + r.y ) * sj ) );
                }

            int x = center.x + (int)( u.x * radius );
            int y = center.y + (int)( u.y * radius );

            line( x - 2, y, x + 2, y );
            line( x, y - 2, x, y + 2 );
            }        
        }
      
      
    private void drawLine( float x1, float y1, float x2, float y2 )
        {
        line( 
            center.x + (int)( x1 * radius ),
            center.y + (int)( y1 * radius ),
            center.x + (int)( x2 * radius ),
            center.y + (int)( y2 * radius )
            );
        }
    
     private void drawArc( unit3 A, unit3 B )  // Connect points on the sphere.
        {
        int steps = 10;
        float c = (float)( 1.0 / steps );
        M.set( A.x + B.x, A.y + B.y, A.z + B.z );
        float dx = c * ( M.x - A.x );
        float dy = c * ( M.y - A.y );
        float dz = c * ( M.z - A.z );
        P.set( A );
        for( int i = 1; i <= steps; i++ )  // Draw the first half.
            {
            Q.set( A.x + i * dx, A.y + i * dy, A.z + i * dz );
            drawLine( P.x, P.y, Q.x, Q.y );
            P.x = Q.x;
            P.y = Q.y;
            }
        dx = c * ( B.x - M.x );
        dy = c * ( B.y - M.y );
        dz = c * ( B.z - M.z );
        P.set( M );
        for( int i = 1; i <= steps; i++ )  // Draw the second half.
            {
            Q.set( M.x + i * dx, M.y + i * dy, M.z + i * dz );
            drawLine( P.x, P.y, Q.x, Q.y );
            P.x = Q.x;
            P.y = Q.y;
            }
        }
    
     private void drawTriangle() 
        {
            if( !showCircles ) return;
            fill(125,255,166);
            strokeWeight(4);
            stroke(125,255,166);
            drawArc( A, B );
            drawArc( B, C );
            drawArc( C, A );
            strokeWeight(1);
        }
     
    private void drawSphere() 
        {
        fill(255,231,125,200);
        stroke(255,231,125,255);
         ellipseMode(CORNER);
       ellipse( 
            center.x - radius, 
            center.y - radius, 
            diameter,
            diameter
            );
        }

     private void markVertex( unit3 A,int rad )
        {
        int x = center.x + (int)( A.x * radius );
        int y = center.y + (int)( A.y * radius );
                 ellipseMode(CORNER);
       ellipse( x - rad, y - rad, 2 * rad, 2 * rad );
        }
     
         private void markVertices()
        {
        fill(125,149,255);
         stroke(125,149,255);
            markVertex( A,  10 );
            markVertex( B, 10 );
            markVertex( C,  10 );
        }
         
         private void drawXYAxes()
        {
            strokeWeight(2);
            stroke(255,0,0,255);
            fill(255,0,0,255);
           drawLine(0,0,1.1f,0);
           triangle(
                   center.x + (int)(1.2 * radius ),center.y,
                   center.x + (int)(1.1 * radius ),center.y + (int)(0.025 * radius ),
                    center.x + (int)(1.1 * radius ),center.y - (int)(0.025 * radius ));
            stroke(0,255,0,255);
            fill(0,255,0,255);
           drawLine(0,0,0,1.1f);
            triangle(
                   center.x ,center.y + (int)(1.2 * radius ),
                   center.x + (int)(0.025 * radius ),center.y + (int)(1.1 * radius ),
                    center.x - (int)(0.025 * radius ),center.y + (int)(1.1 * radius ));
           strokeWeight(1);
        }
         private void drawZAxis()
        {
            stroke(0,0,255,255);
            fill(0,0,255,170);
            markVertex( new unit3(0,0,0),(int)(0.025 * radius ) );
            strokeWeight(2);
            drawLine(0,0,0,0);
            strokeWeight(1);
        }


         
    public void draw() 
    {
        background(255);
        smooth();
        
        drawXYAxes();
        drawSphere();
        drawZAxis();
        
        drawTriangle();
        markVertices();
        
        drawPoints();
        
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
            System.err.println("SHOW CIRCLEs");
        }
        else if (key == ' ') {
            //arcball.switchActive();
            //spot2.setVisible(!spot2.isVisible());
        }

    }
    
    
    public void Resample()
        {
        int k = 0;
        show_pts = true;
        for( int i = 0; i < npts; i++ )
        for( int j = 0; j < npts; j++ )
            {
            float x = rng.nextFloat();
            float y = rng.nextFloat();
            ( (vec2)coords.elementAt(k++) ).set( x, y );
            }        
        }
}
