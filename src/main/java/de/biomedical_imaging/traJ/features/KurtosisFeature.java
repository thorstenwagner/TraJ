package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.MomentsCalculator;

/**
 * Evaluates the skewness of the trajectory. Therefore the radius of gyration tensor is estimated.
 * For the calculation, the positions are projected on the dominant eigenvector of the radius of gyration
 * tensor.:
 * Skewness = 1/N sum( (x_i - mean)^3 / sd^3)
 * where N is the number of positions, mean the mean position and sd the standard deviation
 * @author Thorsten Wagner
 *
 */
public class KurtosisFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public KurtosisFeature(Trajectory t) {
		this.t = t;
	}
	
	
	@Override
	public double[] evaluate() {
		MomentsCalculator moments = new MomentsCalculator(t);
		return new double[] {moments.calculateNthMoment(4)};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Kurtosis";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "KURT";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t =t ;
		
	}

}
