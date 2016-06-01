package de.biomedical_imaging.traJ.features;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.RadiusGyrationTensor2D;

/**
 * The features characterizes  the asymetry of the trajectory. 
 * 
 * It was implemented according to
 * 
 * Helmuth, J.A. et al., 2007. 
 * A novel supervised trajectory segmentation algorithm identifies distinct types of human adenovirus motion in host cells. 
 * Journal of structural biology, 159(3), pp.347–58.
 * @author Thorsten Wagner
 *
 */
public class Asymmetry3Feature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public Asymmetry3Feature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2D.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		double e1 = eigdec.getRealEigenvalue(0);
		double e2 = eigdec.getRealEigenvalue(1);
		double asym = -1*Math.log(1-Math.pow(e1-e2,2)/(2*Math.pow(e1+e2, 2)));
		return new double[]{asym};
	}

	@Override
	public String getName() {
		return "Assymetry3";
	}

	@Override
	public String getShortName() {
		return "ASYM3";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
