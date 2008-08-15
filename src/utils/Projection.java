/*
 * Projection.java
 *
 * Created on 05 June 2008, 12:27
 */

package utils;

import org.apache.commons.math.geometry.Vector3D;

/**
 * A wrapper for various static methods for 3D projection geometry
 *
 * @author Nicolas Van Labeke
 */
public class Projection {
    
    /**
     * Creates a new instance of Projection
     */
    public Projection() {}
    
    /**
     * Get the 3D point from its projection on the XYU screen of the wiimote
     */
    public static Vector3D get3DFromXY(double X,double Y) {
        Vector3D vec = new Vector3D();
        
        //System.out.println("X: " + X + " Y: " + Y);
        
        double x = (-1 - Math.sqrt(2))/2. + (0.006035533905932738*
                (0.007285533905932737 + Math.sqrt(0.00005307900429449553 -
                (-0.25 + Math.pow(1 + Math.sqrt(2),2)/4.)*
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11 * Math.pow(384 - Y,2.0)))))/
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11*Math.pow(384 - Y,2));
        
        double y = (6.510416666666666e-6*(-512 + X)*(0.007285533905932737 +
                Math.sqrt(0.00005307900429449553 - (-0.25 + Math.pow(1 + Math.sqrt(2),2)/4.)*
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11*Math.pow(384 - Y,2)))))/
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11*Math.pow(384 - Y,2));
        
        double z = (6.510416666666666e-6*(0.007285533905932737 +
                Math.sqrt(0.00005307900429449553 - (-0.25 + Math.pow(1 + Math.sqrt(2),2)/4.)*
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11*Math.pow(384 - Y,2))))*(384 - Y))/
                (0.00003642766952966369 + 4.238552517361111e-11*Math.pow(-512 + X,2) +
                4.238552517361111e-11*Math.pow(384 - Y,2));
        
        //System.out.println("x: " + x + " y: " + y + " z:" + z);
        if (Double.isNaN(x) || Double.isNaN(y)  || Double.isNaN(z) )
            return null;
        else
            //return new Vector3D((float)x,(float)y,(float)z);
            return new Vector3D((float)y,(float)z,(float)x);
    }
    
    class CPair{
        int nbint;
        Vector3D pt1;
        Vector3D pt2;
    }
    
    
     /**
      *@deprecated. Not in use anymore. Prefer the get3DFromXY() method.
     * Get the intersection between a line and a sphere
     */
   private CPair IntersectLine(float x1,float y1, float radius) {
        CPair pair = new CPair();
        
        Vector3D raybase = new Vector3D(x1,y1,0);
        Vector3D raybase2 = new Vector3D(x1,y1,1);
        Vector3D raycos = raybase2.subtract(raybase);
        raycos.normalize();
        Vector3D center = new Vector3D(0,0);
        
        boolean hit;			/* True if ray intersects sphere*/
        double	dx, dy, dz;		/* Ray base to sphere center	*/
        double	bsq, u, disc;
        double	root,rin,rout;
        
        
        dx   = raybase.getX() - center.getX();
        dy   = raybase.getY() - center.getY();
        dz   = raybase.getZ() - center.getZ();
        bsq  = dx*raycos.getX() + dy*raycos.getY() + dz*raycos.getZ();
        u    = dx*dx + dy*dy + dz*dz - radius*radius;
        disc = bsq*bsq - u;
        if (Math.abs((double)disc)<1E-5) disc = 0.0f;
        hit  = (disc >= -1E-5);
        pair.nbint = (hit)? 1:0;
        if  (hit) { 				/* If ray hits sphere	*/
            root  =  (float)Math.sqrt(disc);
            rin  = -bsq - root;		/*    entering distance	*/
            rout = -bsq + root;		/*    leaving distance	*/
            
            Vector3D dir2 = raycos.scalarMultiply(rin);
            pair.pt1 = raybase.add(dir2);
            dir2 = raycos.scalarMultiply(rout);
            pair.pt2 = raybase.add(dir2);
            
        }
        return (pair);
    }
}
