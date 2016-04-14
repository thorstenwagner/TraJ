package de.biomedical_imaging.traJ.features;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.RadiusGyrationTensor2D;


/**
 * The features characterizes  the asymetry of the trajectory. 
 * It is equal to 0 for circularly symmetric trajectories and 
 * 1 for linear trajectories.
 * 
 * It was implemented according to
 * 
 * Saxton, M.J., 1993. Lateral diffusion in an archipelago. 
 * Single-particle diffusion. Biophysical Journal, 64(6), pp.1766–1780.
 * @author Thorsten Wagner
 *
 */
public class Asymmetry2Feature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public Asymmetry2Feature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2D.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		double e1 = eigdec.getRealEigenvalue(0);
		double e2 = eigdec.getRealEigenvalue(1);
		
		double asym = e2/e1; //-1*Math.log(1-Math.pow(e1-e2, 2)/(2*Math.pow(e1+e2, 2)));
		return new double[]{asym};
	}

	@Override
	public String getName() {
		return "Assymetry2";
	}

	@Override
	public String getShortName() {
		return "ASYM2";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
