package de.biomedical_imaging.traj.math;

import javax.vecmath.Vector2d;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Evaluates the nTh moment of the trajectory. Therefore the radius of gyration tensor is estimated.
 * For the calculation, the positions are projected on the dominant eigenvector of the radius of gyration
 * tensor.:
 * nTh moment  = 1/N sum( (x_i - mean)^n / sd^n)
 * 
 * where n is order of moment, N the number of positions, mean the mean position and sd the standard deviation
 * 
 * The feature was implemented according to:
 * Helmuth, J.A. et al., 2007. 
 * A novel supervised trajectory segmentation algorithm identifies 
 * distinct types of human adenovirus motion in host cells. 
 * Journal of structural biology, 159(3), pp.347–58.
 * 
 * @author Thorsten Wagner 
 *
 */
public class MomentsCalculator {
	
	private Trajectory t;
	
	public MomentsCalculator(Trajectory t) {
		this.t = t;
	}
	
	public double calculateNthMoment(int n){
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2D.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		
		Vector2d eigv = new Vector2d(eigdec.getEigenvector(0).getEntry(0),eigdec.getEigenvector(0).getEntry(1));

		double[] projected = new double[t.size()];
		for(int i = 0; i < t.size(); i++){
			Vector2d pos = new Vector2d(t.get(i).x,t.get(i).y);
			double v = eigv.dot(pos);
			projected[i] = v;
		}
		
		Mean m = new Mean();
		StandardDeviation s = new StandardDeviation();
		double mean = m.evaluate(projected);
		double sd  = s.evaluate(projected);
		double sumPowN=0;

		for(int i = 0; i < projected.length; i++){
			sumPowN += Math.pow( (projected[i]-mean)/sd, n);
		}

		double nThMoment =  sumPowN/projected.length;
		
		return nThMoment;
	}
	

}
