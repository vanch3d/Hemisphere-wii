/*
 * Hemisphere.java
 *
 * Created on 04 June 2008, 10:58
 */

package surface;

import processing.core.PGraphics;
import surface.calculation.CosinusTable;
import surface.calculation.LookUpTable;
import surface.calculation.SinusTable;

/**
 * Surface representing an hemisphere
 *
 * @author Nicolas Van Labeke
 */
public class Hemisphere extends Surface  {
    
	private LookUpTable sinThetaTable;

	private LookUpTable cosThetaTable;

	private LookUpTable sinPhiTable;

	private LookUpTable cosPhiTable;

	/**
	 * @param i_g
	 *            A PGraphics object where the surface should be drawn in.
	 *            Mostly this is g, the current PGraphics object of your sketch.
	 * @param i_phiSteps
	 *            The horizontal resolution of the surface.
	 * @param i_thetaSteps
	 *            The vertical resolution of the surface.
	 * @param i_colors
	 * 				An array which holds the colors for the vertical and horizontal gradient ([[[verticalColor1],[verticalColor2],...],[[horizontalColor1],[horizontalColor2]]]). 
	 * 				You can also use [[[verticalColor1],[verticalColor2],...],null]) to get only an vertical gradient. Note that the the color stuff only work in the OPENGL mode.
	 *           	
	 * @releated surface 
	 
	 */
	public Hemisphere(
			final PGraphics i_g, 
			final int i_phiSteps,
			final int i_thetaSteps, 
			final int[][] i_colors) {
		
		super(
				i_g, 
				i_phiSteps, 
				i_thetaSteps, 
				0, PI/2, 0, TWO_PI, 
				null, 
				i_colors);
	}
	
	public Hemisphere(
			final PGraphics i_g, 
			final int i_phiSteps,
			final int i_thetaSteps) {
		
		this(
				i_g, 
				i_phiSteps, 
				i_thetaSteps,  
				null
			);
	}
	protected void initValues() {
		sinThetaTable = new SinusTable(thetaSteps, minTheta, maxTheta);
		cosThetaTable = new CosinusTable(thetaSteps, minTheta, maxTheta);
		sinPhiTable = new SinusTable(phiSteps, minPhi, maxPhi);
		cosPhiTable = new CosinusTable(phiSteps, minPhi, maxPhi);
	}

	protected float calculateX(final int i_phiStep, final int i_thetaStep) {
		return sinThetaTable.getValue(i_thetaStep)
				* cosPhiTable.getValue(i_phiStep);
	}

	protected float calculateY(final int i_phiStep, final int i_thetaStep) {
		return sinThetaTable.getValue(i_thetaStep)
				* sinPhiTable.getValue(i_phiStep);
	}

	protected float calculateZ(final int i_phiStep, final int i_thetaStep) {
		return cosThetaTable.getValue(i_thetaStep);
	}    


}
