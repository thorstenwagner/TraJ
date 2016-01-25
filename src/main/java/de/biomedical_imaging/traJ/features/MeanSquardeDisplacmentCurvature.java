package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * Implements the msd curvature feature according
 * S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, 
 * “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” 
 * Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.
 * @author Thorsten Wagner
 *
 */
public class MeanSquardeDisplacmentCurvature extends AbstractTrajectoryFeature {
	private Trajectory t;
	
	public MeanSquardeDisplacmentCurvature(Trajectory t){
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		
		
		int Ndiff = 2;
		if((int)(t.size()*0.1)>2){
			Ndiff = (int)(t.size()*0.05);
		}
		RegressionDiffusionCoefficientEstimator regEst = new RegressionDiffusionCoefficientEstimator(1, Ndiff);
		double[] regline = regEst.getDiffusionCoefficient(t, 1);
		double slope = regline[1];
		double intercept = regline[2];
		
		int Ndef = 10*Ndiff;
		if(Ndef>t.size()/2){
			Ndef = t.size()/2;
		}
		MeanSquaredDisplacmentFeature msd = new MeanSquaredDisplacmentFeature(t, 1);
		double sum = 0;
		for(int i = 1; i <= Ndef; i++){
			msd.setTimalag(i);
			sum+= (msd.evaluate()[0] - evaluateMSDLine(i, slope, intercept))/evaluateMSDLine(i, slope, intercept);
		}
		double dev = sum/Ndef;
		return new double[] {dev};
	}
	
	private double evaluateMSDLine(double v, double slope, double intercept){
		return v*slope + intercept;
	}

	@Override
	public String getName() {
		
		return "Mean Squared Displacment Curvature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
	}

	
}