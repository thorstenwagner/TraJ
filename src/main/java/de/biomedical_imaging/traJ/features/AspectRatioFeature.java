package de.biomedical_imaging.traJ.features;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import cg.RotatingCalipers;
import de.biomedical_imaging.traJ.Trajectory;

public class AspectRatioFeature extends AbstractTrajectoryFeature {
	private Trajectory t; 
	
	public AspectRatioFeature(Trajectory t) {
		this.t = t;
		if(t.getDimension()!=2){
			throw new IllegalArgumentException("Works only with 2D trajectorys");
		}
	}
	@Override
	public double[] evaluate() {
		
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		for(int i = 0; i < t.size(); i++){
			Point2D.Double p = new Point2D.Double();
			p.setLocation(t.get(i).x, t.get(i).y);
			points.add(p);
		}
		Point2D.Double[] rect =null;
		try{
			rect = RotatingCalipers.getMinimumBoundingRectangle(points);
		}
		catch(IllegalArgumentException e){
			//If the trajectory consits of colinear points, return an aspect ratio of infinity
			return new double[] {Double.POSITIVE_INFINITY};
		}

		double longSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[0].distance(rect[1]):rect[1].distance(rect[2]);
		double shortSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[1].distance(rect[2]):rect[0].distance(rect[1]);
		result = new double[] {longSide/shortSide};
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Abstract ratio";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}
	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "AR";
	}

}
