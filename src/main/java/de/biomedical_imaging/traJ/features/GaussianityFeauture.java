package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Implements the feature "Gaussianity" as descriped in 
 * Ernst, D., Köhler, J. & Weiss, M., 2014. Probing the 
 * type of anomalous diffusion with single-particle tracking. 
 * Physical chemistry chemical physics : PCCP, 16(17), pp.7686–91.
 * 
 * @author Thorsten Wagner 
 *
 */
public class GaussianityFeauture extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int timelag;
	private String name = "Gaussianity";
	private String sname = "GAUSS";
	
	public GaussianityFeauture(Trajectory t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	@Override
	public double[] evaluate() {
		MeanSquaredDisplacmentFeature msdf = new MeanSquaredDisplacmentFeature(t, timelag);
		QuartricMomentFeature qart = new QuartricMomentFeature(t, timelag);
		
		double msd = msdf.evaluate()[0];
		double q = qart.evaluate()[0];
		
		double res = (2*q)/(3*msd*msd) - 1;
		return new double[] {res};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return sname;
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
