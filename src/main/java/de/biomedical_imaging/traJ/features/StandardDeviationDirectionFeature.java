package de.biomedical_imaging.traJ.features;

import java.util.Vector;

import javax.vecmath.Vector3d;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Implements the standard deviation of the trajectory direction according to 
 * [1] D. Arcizet, B. Meier, E. Sackmann, J. O. Rädler, and D. Heinrich, 
 * “Temporal analysis of active and passive transport in living cells,” 
 * Phys. Rev. Lett., vol. 101, no. 24, p. 248103, Dec. 2008.
 * @author Thorsten Wagner
 *
 */
public class StandardDeviationDirectionFeature extends AbstractTrajectoryFeature {
	private Trajectory t;
	private int timelag;
	public StandardDeviationDirectionFeature(Trajectory t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	public double[] evaluate() {
		StandardDeviation sd = new StandardDeviation();
		double[] values = new double[t.size()-timelag];
		double subx = 0;
		double suby = 0;
		double subz = 0;
		for(int i = timelag+1; i < t.size(); i++){
			
				subx = t.get(i-timelag-1).x;
				suby = t.get(i-timelag-1).y;
				subz = t.get(i-timelag-1).z;
			
			Vector3d v1 = new Vector3d(t.get(i-timelag).x-subx,t.get(i-timelag).y-suby,t.get(i-timelag).z-subz);
			
			subx = t.get(i-1).x;
			suby = t.get(i-1).y;
			subz = t.get(i-1).z;
			Vector3d v2 = new Vector3d(t.get(i).x-subx,t.get(i).y-suby,t.get(i).z-subz);
		
			double v = v1.angle(v2);
			if(Double.isNaN(v)){
				System.out.println("v1 x" + v1.x + " y" + v1.y + " z: " + v1.z);
				System.out.println("v2 x" + v2.x + " y" + v2.y + " z: " + v2.z);
			}
			values[i-timelag] = v;
			//System.out.println("da " + v1.angle(v2));
		}
		sd.setData(values);
		return new double[]{sd.evaluate()};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Standard deviation direction-dt-"+timelag;
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
