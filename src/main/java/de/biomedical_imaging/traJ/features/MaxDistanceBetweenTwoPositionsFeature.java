package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public class MaxDistanceBetweenTwoPositionsFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public MaxDistanceBetweenTwoPositionsFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		double maxDistance = Double.MIN_VALUE;
		for(int i = 0; i < t.size(); i++){
			for(int j = i+1; j< t.size(); j++){
				double d = t.get(i).distance(t.get(j));
				if(d> maxDistance){
					maxDistance = d;
				}
			}
		}
		return new double[] {maxDistance};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Maximum distance between two positions";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MAX-DIST-POS";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
	}

}
