package de.biomedical_imaging.traJ;

import javax.vecmath.Point3d;

public class TrajectoryUtil {
	
	public static Trajectory combineTrajectory(Trajectory a, Trajectory b){
		if(a.getDimension()!=b.getDimension()){
			throw new IllegalArgumentException("Combination not possible: The trajectorys does not have the same dimension");
		}
		if(a.getPositions().size()!=b.getPositions().size()){
			throw new IllegalArgumentException("Combination not possible: The trajectorys does not "
					+ "have the same number of steps a="+a.getPositions().size() + " b="+b.getPositions().size());
		}
		Trajectory c = new Trajectory(a.getDimension());
		
		for(int i = 0 ; i < a.getPositions().size(); i++){
			Point3d pos = new Point3d(a.getPositions().get(i).x+b.getPositions().get(i).x, 
					a.getPositions().get(i).y+b.getPositions().get(i).y, 
					a.getPositions().get(i).z+b.getPositions().get(i).z);
			c.addPosition(pos);
		}
		
		return c;
	}

}
