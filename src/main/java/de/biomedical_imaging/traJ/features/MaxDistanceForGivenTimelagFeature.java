package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public class MaxDistanceForGivenTimelagFeature extends AbstractTrajectoryFeature {
	private Trajectory t;
	private int lag;
	
	public MaxDistanceForGivenTimelagFeature(Trajectory t, int lag) {
		this.t = t;
		this.lag = lag;
	}
	@Override
	public double[] evaluate() {
		double distance = Double.MIN_NORMAL;
		for(int i = lag; i < t.size(); i++){
			double d = t.get(i-lag).distance(t.get(i)); 
			if(d> distance){
				distance = d;
			}
		}
		return new double[]{distance};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Max distance for a given timelag";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MAX-DIST-LAG";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
