package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

public class BoundednessFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private double timelag;
	
	public BoundednessFeature(Trajectory t, double timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	public double[] evaluate() {
		MaxDistanceBetweenTwoPositionsFeature dtwop = new MaxDistanceBetweenTwoPositionsFeature(t);
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(t, 1/timelag, 1, 2);
		
		double D = dcEst.evaluate()[0];
		double r = dtwop.evaluate()[0]/2;
		double cov_area = a(t.size())*D*timelag;
		double res = cov_area/(4*r*r);
		result = new double[]{res};
		return result;
	}
	
	public static double a(int length){
		return -79.751+4.051*length;
	}

	@Override
	public String getName() {
		return "Boundedness";
	}

	@Override
	public String getShortName() {
		return "BOUND";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
