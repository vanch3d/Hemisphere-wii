/*
 * Cone.java
 *
 * Created on 05 June 2008, 12:42
 */

package surface;

import processing.core.PGraphics;
import surface.calculation.CosinusTable;
import surface.calculation.LookUpTable;
import surface.calculation.SinusTable;

/**
 * A cone as a Surface object
 *
 * @deprecated
 * @author Nicolas Van Labeke
 */
public class Cone extends Surface  {
    
    private LookUpTable sinThetaTable;

    private LookUpTable cosThetaTable;

    /** Creates a new instance of Cone */
    public Cone(final PGraphics i_g, 
			final int i_phiSteps,
			final int i_thetaSteps) 
    {
        super(i_g, 
                i_phiSteps, 
		i_thetaSteps, 
		0, PI/2, 0, TWO_PI, 
		null, 
		null);
    }
    
}
