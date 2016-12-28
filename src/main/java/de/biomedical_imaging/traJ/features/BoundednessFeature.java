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
		double res = calculateBoundedness(D, t.size(), timelag, r); 
		result = new double[]{res};
		return result;
	}
	
	/**
	 * Calculates the Boundedness value to given confinement radius, diffusion coefficient, timlag and number of steps.
	 * @param D diffusion coefficient
	 * @param N Number of steps
	 * @param timelag Timelag
	 * @param confRadius Confinement radius
	 * @return Boundedness value
	 */
	public static double calculateBoundedness(double D, int N, double timelag, double confRadius){
		double r = confRadius;
		double cov_area = a(N)*D*timelag;
		double res = cov_area/(4*r*r);
		return res;
	}
	
	/**
	 * Calculates the radius to a given boundedness value
	 * @param D Diffusion coefficient
	 * @param N Number of steps
	 * @param timelag Timelag
	 * @param B Boundedeness
	 * @return Confinement radius
	 */
	public static double getRadiusToBoundedness(double D, int N, double timelag, double B){
		double cov_area = a(N)*D*timelag;
		double radius = Math.sqrt(cov_area/(4*B));
		return radius;
	}
	public static double a(int length){
		return 4*length;//-79.751+4.051*length;
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
