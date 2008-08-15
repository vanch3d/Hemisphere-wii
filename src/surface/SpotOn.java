/*
 * SpotOn.java
 *
 * Created on 05 June 2008, 11:08
 */

package surface;

import org.apache.commons.math.geometry.Vector3D;
import processing.core.PGraphics;


/**
 * Extension of a sphere surface to represent a "spot", i.e a marker on the hemipshere
 *
 * @author Nicolas Van Labeke
 */
public class SpotOn extends Sphere {
    
    Vector3D spotVector;
    boolean isVisible;
    
    /** Creates a new instance of SpotOn */
    public SpotOn(  final PGraphics i_g,
            final int i_phiSteps,
            final int i_thetaSteps) {
        super(i_g, i_phiSteps, i_thetaSteps);
        this.spotVector = new Vector3D(0,0,0);
        this.isVisible = true;
    }
    
    public SpotOn(  final PGraphics i_g,
            final int i_phiSteps,
            final int i_thetaSteps,
            final Vector3D vec) {
        super(i_g, i_phiSteps, i_thetaSteps);
        this.spotVector = vec;
        this.isVisible = true;
    }
    
    public Vector3D getVector()
    {
        return spotVector;
    }
    
    public void setVector(Vector3D vec)
    {
        this.spotVector = vec;
    }

    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean vis)
    {
        this.isVisible = vis;
    }
}
