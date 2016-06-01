package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.MomentsCalculator;

/**
 * Evaluates the kurtosis of the trajectory. Therefore the radius of gyration tensor is estimated.
 * For the calculation, the positions are projected on the dominant eigenvector of the radius of gyration
 * tensor.:
 * Kurtosis = 1/N sum( (x_i - mean)^4 / sd^4)
 * where N is the number of positions, mean the mean position and sd the standard deviation
 * 
 * @author Thorsten Wagner
 *
 */
public class SkewnessFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public SkewnessFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		MomentsCalculator moments = new MomentsCalculator(t);
		return new double[] {moments.calculateNthMoment(3)};
	}

	@Override
	public String getName() {
		
		return "Skewness";
	}

	@Override
	public String getShortName() {

		return "SKEW";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t =t;
		
	}

}
