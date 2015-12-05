package de.biomedical_imaging.traJ;

import java.util.ArrayList;
import javax.vecmath.Point3d;

public  class Trajectory {
	
	private ArrayList<Point3d> positions;

	private int dimension;
	
	public Trajectory(int dimension) {
		this.dimension = dimension;
		positions = new ArrayList<Point3d>();
	}
	
	public  ArrayList<Point3d> getPositions(){
		return positions;
	}
	
	/**
	 * Adds an position the trajectory. Between two position should the same timelag.
	 * If there is a gap in the trajectory, please add null.
	 * @param pos The next position of the trajectory. Add null for a gap.
	 */
	public void addPosition(Point3d pos){
		positions.add(pos);
	}
	
	public int getDimension(){
		return dimension;
	}
	
}
