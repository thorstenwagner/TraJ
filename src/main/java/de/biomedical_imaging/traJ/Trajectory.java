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
	
	public void addPosition(Point3d pos){
		positions.add(pos);
	}
	
	public int getDimension(){
		return dimension;
	}
	
}
