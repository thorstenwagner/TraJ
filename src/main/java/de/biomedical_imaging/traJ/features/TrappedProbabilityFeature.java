package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.CovarianceDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * Following Saxton [1], the probability p that a trajectory is trapped
 * inside a radius R is:
 *   p = 1 - exp(0.2048-2.5117(D*t/R^2))
 * where D is the diffusion coefficient, and t is time duration of the trajectory.
 * 
 * For this feature R is estiamted by the maximum distance between two positions and
 * D is estimated by the the covariance estimator.
 * @author thorsten
 *
 */
public class TrappedProbabilityFeature extends AbstractTrajectoryFeature{
	private Trajectory t;
	
	/**
	 * 
	 * @param t Trajectory
	 * @param timelag Timelag between two steps
	 */
	public TrappedProbabilityFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		MaxDistanceBetweenTwoPositionsFeature dtwop = new MaxDistanceBetweenTwoPositionsFeature(t);
		double r = dtwop.evaluate()[0]/2;
		
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator(t, 1);
		
		double D = dcEst.evaluate()[0];
		double time = t.size();

		double p = 1- Math.exp(0.2048-2.5117*(D*time/(r*r)));
		return new double[]{p};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Trapped trajectory probability";
	}

	@Override
	public String getShortName() {
		return "TRAPPED";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
