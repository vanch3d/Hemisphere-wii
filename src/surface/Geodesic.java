/*
 * Geodesic.java
 *
 * Created on 05 June 2008, 12:14
 */

package surface;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import processing.core.PGraphics;

/**
 * Extension of a torus surface to represent a geodesic between two points on a sphere (SpotOn)
 *
 * @author Nicolas Van Labeke
 */
public class Geodesic extends Torus {
    
    private SpotOn spot1;
    private SpotOn spot2;
    
    /** Creates a new instance of Geodesic */
    public Geodesic(final PGraphics i_g,final int i_phiSteps,
            final int i_thetaSteps) {
        
       super(i_g, i_phiSteps, i_thetaSteps);
       spot1 = spot2 = null;
    }
    
   /** Creates a new instance of Geodesic */
    public Geodesic(final PGraphics i_g,final int i_phiSteps,
            final int i_thetaSteps,SpotOn src1, SpotOn src2) {
        
       super(i_g, i_phiSteps, i_thetaSteps);
       spot1 = src1;
       spot2 = src2;
    }

    public void draw() 
    {
        if (spot1== null || spot2==null) return;
        if (!spot1.isVisible || !spot2.isVisible()) return;
               //translate(-spot2.getVector().x,-spot2.getVector().y,-spot2.getVector().z);
           Vector3D u = spot1.getVector();
            //u.mult(-1);`
           // u=u.normalize();
            Vector3D v = spot2.getVector();
            // v.mult(-1);
           // v=v.normalize();
            //Vector3D w = Vector3D.crossProduct(u,v);
            //w.normalize();
            double gg = Vector3D.angle(u,v);
               Rotation rotU = new Rotation(new Vector3D(0,0,1),gg);
               Vector3D ww = rotU.applyTo(new Vector3D(1,0,0));
            
               
            Rotation rot = new Rotation(
                    new Vector3D(1,0,0),ww,
                    u,v);
            float angle = (float)rot.getAngle();
            Vector3D axis = rot.getAxis();
            
           /* Vector3D marker = w;//Vector3D.mult(w,(radius*1.2f));
            //System.out.println("marker2 X: " +  marker.x + " Y: " +  marker.y + " (" +  marker.z + ")");
            float r = (float)Math.sqrt(marker.getX()*marker.getX()+marker.getY()*marker.getY()+marker.getZ()*marker.getZ()); // radial distance
            float theta = (float)Math.atan2(marker.getY(),marker.getX()); // zenith angle, range -pi..pi
            float phi = (float)Math.acos(marker.getZ()/r); // azimuth angle, range -pi/2..pi/2
            g.rotateZ(theta); // "heading" or "around"
            g.rotateY(phi);  // "tilt" or "elevation"
            */
           // this.draw();
           //double gg = Vector3D.angle(u,v);
           double nbs = gg*phiSteps()/(2*g.PI);
        
            //super.draw();
            g.pushMatrix();     
            g.noStroke();
            g.fill(125,255,166);
            g.rotate(angle,(float)axis.getX(),(float)axis.getY(),(float)axis.getZ());
            //System.out.println("marker1 X: " +  w.x + " Y: " +  w.y + " (" +  w.z + ")");
            super.drawPart(0,(int)nbs,0,thetaSteps());
            g.noStroke();
            g.fill(125,255,166,100);
            super.drawPart((int)nbs,phiSteps(),0,thetaSteps());
          
            g.popMatrix();
    }
    
}
