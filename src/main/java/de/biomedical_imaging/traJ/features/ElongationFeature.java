package de.biomedical_imaging.traJ.features;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import cg.RotatingCalipers;
import de.biomedical_imaging.traJ.Trajectory;

/**
 * Estimates the the elongation by 1 - S/L where S is the short side
 * and L the long side of the minimum bounding rectangle. When the trajectory
 * consits of colinear points, the elgonation is 1 by default.
 * @author Thorsten Wagner
 *
 */
public class ElongationFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public ElongationFeature(Trajectory t) {
		this.t = t;
	}
	
	/**
	 * Estimates the the elongation by 1 - S/L where S is the short side
	 * and L the long side of the minimum bounding rectangle. When the trajectory
	 * consits of colinear points, the elgonation is 1 by default.
	 */
	@Override
	public double[] evaluate() {
		
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		for(int i = 0; i < t.size(); i++){
			Point2D.Double p = new Point2D.Double();
			p.setLocation(t.get(i).x, t.get(i).y);
			points.add(p);
		}
		Point2D.Double[] rect = null;
		try{
			rect = RotatingCalipers.getMinimumBoundingRectangle(points);
		}
		catch(IllegalArgumentException e){
			return new double[] {1};
		}

		double longSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[0].distance(rect[1]):rect[1].distance(rect[2]);
		double shortSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[1].distance(rect[2]):rect[0].distance(rect[1]);
		return new double[] {1-shortSide/longSide};
	}

	@Override
	public String getName() {
		
		return "Elongation";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
