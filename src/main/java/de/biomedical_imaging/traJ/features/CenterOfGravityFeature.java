package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public class CenterOfGravityFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public CenterOfGravityFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		double x = 0;
		double y = 0;
		double z = 0;
		
		for(int i = 0; i < t.size(); i++){
			x += t.get(i).x;
			y += t.get(i).y;
			z += t.get(i).z;
		}
		
		x = x/t.size();
		y = y/t.size();
		z = z/t.size();
		
		return new double[] {x,y,z};
	}

	@Override
	public String getName() {
		return "Center of gravity";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "COG";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
