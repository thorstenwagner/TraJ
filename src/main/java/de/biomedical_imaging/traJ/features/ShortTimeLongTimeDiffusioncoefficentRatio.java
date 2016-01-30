package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * The ratio of the long time diffusion coefficent and the short  time diffusion coefficient
 * @author thorsten
 *
 */
public class ShortTimeLongTimeDiffusioncoefficentRatio extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int numberOfPoints;
	
	public ShortTimeLongTimeDiffusioncoefficentRatio(Trajectory t, int numberOfPoints) {
		this.t = t;
		this.numberOfPoints = numberOfPoints;
	}
	
	@Override
	public double[] evaluate() {
		RegressionDiffusionCoefficientEstimator rgShort = new RegressionDiffusionCoefficientEstimator(1, 1+numberOfPoints);
		RegressionDiffusionCoefficientEstimator rgLong = new RegressionDiffusionCoefficientEstimator(t.size()/10 - numberOfPoints, t.size()/10);
		double Dshort = rgShort.getDiffusionCoefficient(t, 1)[0];
		double Dlong = rgLong.getDiffusionCoefficient(t, 1)[0];
		return new double[]{Dlong/Dshort};
	}

	@Override
	public String getName() {
		
		return "Short time / long time diffusion coefficient ratio";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "StLtDcRatio";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
