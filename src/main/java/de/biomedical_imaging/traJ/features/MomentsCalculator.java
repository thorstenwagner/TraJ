package de.biomedical_imaging.traJ.features;

import ij.IJ;

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
		Array2DRowRealMatrix gyr = getRadiusOfGyrationTensor();
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
	
	/**
	 * Calculates the radius of gyration tensor according to formula (6.3) in
	 * 
	 * ELEMENTS OF THE RANDOM WALK by Rudnick and Gaspari
	 * 
	 * @return Radius of gyration tensor
	 */
	public Array2DRowRealMatrix getRadiusOfGyrationTensor(){
		double meanx =0;
		double meany =0;
		for(int i = 0; i < t.size(); i++){
			meanx+= t.get(i).x;
			meany+= t.get(i).y;
		}
		meanx = meanx/t.size();
		meany = meany/t.size();
		
		double e11 = 0;
		double e12 = 0;
		double e21 = 0;
		double e22 = 0;
		
		for(int i = 0; i < t.size(); i++){
			e11 += Math.pow(t.get(i).x-meanx,2);
			e12 += (t.get(i).x-meanx)*(t.get(i).y-meany);
			e22 += Math.pow(t.get(i).y-meany,2);
		}
		e11 = e11 / t.size();
		e12 = e12 / t.size();
		e21 = e12;
		e22 = e22 / t.size();
		int rows = 2;
		int columns = 2;
		Array2DRowRealMatrix gyr = new Array2DRowRealMatrix(rows, columns); 
		
		gyr.addToEntry(0, 0, e11);
		gyr.addToEntry(0, 1, e12);
		gyr.addToEntry(1, 0, e21);
		gyr.addToEntry(1, 1, e22);
		
		return gyr;
	}

}
