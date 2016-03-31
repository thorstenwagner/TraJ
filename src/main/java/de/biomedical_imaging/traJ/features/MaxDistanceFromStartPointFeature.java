package de.biomedical_imaging.traJ.features;

import javax.vecmath.Point3d;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Maximum distance from start point
 * @author Thorsten Wagner
 *
 */
public class MaxDistanceFromStartPointFeature extends AbstractTrajectoryFeature{
	Trajectory t;
	
	public MaxDistanceFromStartPointFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		double maxDist = Double.MIN_VALUE;
		Point3d start = t.get(0);
		
		for(int i = 1; i < t.size(); i++){
			double d =start.distance(t.get(i));
			
			if(d > maxDist){
				maxDist = d;
			}
		}
	
		return new double[]{maxDist};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Max distances from start point";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MaxDist";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
