/*
The MIT License (MIT)

Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package de.biomedical_imaging.traJ;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.vecmath.Vector2d;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import cg.RotatingCalipers;
import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

/**
 * Implements the spline fitting procedure according to:
 * Spatial structur analysis of diffusive dynamics according to: 
 * B. R. Long and T. Q. Vu, “Spatial structure and diffusive dynamics 
 * from single-particle trajectories using spline analysis,” 
 * Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
 * 
 * 1. Calculate the minimum bounding rectangle for main direction estimation
 * 1.1 Rotate the trajectory that the main direction is parallel to the x-axis
 * 2. Divide the rectangle in n equal segments
 * 3. Calculate the mean standard deviation over each segment: <s>
 * 4. Build a kd-tree
 * 5. Using the first point f in trajectory and calculate the center of mass
 * of all points around f (radius: 3*<s>))
 * 6. The second point is determined by finding the center-of-mass of particles in the p/2 radian 
 * section of an annulus, r1 < r < 2r1, that is directed toward the angle with the highest number 
 * of particles within p/2 radians.
 * 7. This second point is then used as the center of the annulus for choosing the third point, and the process is repeated (6. & 7.).
 * 
 * @author Thorsten Wagner
 *
 */
public class TrajectorySplineFit {
	private List<Point2D.Double> splineSupportPoints;
	private PolynomialSplineFunction spline = null;
	private Trajectory t;
	private int nSegments;
	private Trajectory rotatedTrajectory;
	private double angleRotated;
	
	/**
	 * @param t The trajectory 
	 * @param nSegments The number of segments which are used to estimated the width of the trajectory
	 */
	public TrajectorySplineFit(Trajectory t, int nSegments) {
		this.t = t;
		this.nSegments = nSegments;
		rotatedTrajectory = new Trajectory(2);
	}
	
	public TrajectorySplineFit(Trajectory t) {
		this.t = t;
		this.nSegments = 7;
		rotatedTrajectory = new Trajectory(2);
	}
	
	public PolynomialSplineFunction calculateSpline(){
		
		
		/*
		 * 1.Calculate the minimum bounding rectangle
		 */
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
		catch(IllegalArgumentException e)
		{
			t.showTrajectory();
			e.printStackTrace();
			throw new IllegalArgumentException("Spline curve estimation does not work with colinear points ");
		}
		
		/*
		 * 1.1 Rotate that the major axis is parallel with the xaxis
		 */
		
		
		Point2D.Double p1 = rect[2]; //oben links
		Point2D.Double p2 = p1.distance(rect[1]) > p1.distance(rect[3]) ? rect[1] : rect[3]; //Point to long side
		Point2D.Double p3 = p1.distance(rect[1]) > p1.distance(rect[3]) ? rect[3] : rect[1]; //Point to short side
		Point2D.Double majorDirection = new Point2D.Double(p2.x-p1.x, p2.y-p1.y);
		double inRad = -1*Math.atan2(majorDirection.y, majorDirection.x);
		boolean doTransform = (Math.abs(Math.abs(inRad)-Math.PI)>0.001);

		if(doTransform)
		{
			angleRotated = inRad;
			for(int i = 0; i < t.size(); i++){
				double x = t.get(i).x;
				double y = t.get(i).y;
				double newX = x*Math.cos(inRad)-y*Math.sin(inRad);
				double newY = x*Math.sin(inRad)+y*Math.cos(inRad);
				rotatedTrajectory.add(newX, newY, 0);
				points.get(i).setLocation(newX, newY);
			}
			for(int i = 0; i < rect.length; i++){
				rect[i].setLocation(rect[i].x*Math.cos(inRad)-rect[i].y*Math.sin(inRad), rect[i].x*Math.sin(inRad)+rect[i].y*Math.cos(inRad));
			}

			p1 = rect[2]; //oben links
			p2 = p1.distance(rect[1]) > p1.distance(rect[3]) ? rect[1] : rect[3]; //Point to long side
			p3 = p1.distance(rect[1]) > p1.distance(rect[3]) ? rect[3] : rect[1]; //Point to short side
		}
		else{
			angleRotated = 0;
			rotatedTrajectory = t;
		}
		
		/*
		 * 2. Divide the rectangle in n equal segments
		 * 2.1 Calculate line in main direction
		 * 2.2 Project the points in onto this line
		 * 2.3 Calculate the distance between the start of the line and the projected point
		 * 2.4 Assign point to segment according to distance of (2.3)
		 */
		
		double segmentWidth = p1.distance(p2)/nSegments;
		
		List<List<Point2D.Double>> pointsInSegments = new ArrayList<List<Point2D.Double>>(nSegments);
		for(int i = 0; i < nSegments; i++){
			pointsInSegments.add(new ArrayList<Point2D.Double>());
		}
		for(int i = 0; i < points.size(); i++){
			Point2D.Double projPoint  = projectPointToLine(p1, p2, points.get(i));
			int index = (int)(p1.distance(projPoint)/segmentWidth);
			
			if(index>(nSegments-1)){
				index = (nSegments-1);
			}
			pointsInSegments.get(index).add(points.get(i));
		}
		
		/*
		 * 3. Calculate the mean standard deviation over each segment: <s>
		 */
		Point2D.Double eMajorP1 = new Point2D.Double(p1.x - (p3.x-p1.x)/2.0,p1.y - (p3.y-p1.y)/2.0); 
		Point2D.Double eMajorP2 = new Point2D.Double(p2.x - (p3.x-p1.x)/2.0,p2.y - (p3.y-p1.y)/2.0); 
		double sumMean=0;
		int Nsum = 0;
		for(int i = 0; i < nSegments; i++){
			StandardDeviation sd = new StandardDeviation();
			double[] distances = new double[pointsInSegments.get(i).size()];
			for(int j = 0; j < pointsInSegments.get(i).size(); j++){
				int factor = 1;
				if(isLeft(eMajorP1, eMajorP2, pointsInSegments.get(i).get(j))){
					factor = -1;
				}
				distances[j] = factor*distancePointLine(eMajorP1, eMajorP2, pointsInSegments.get(i).get(j));
			}
			if(distances.length >0){
				sd.setData(distances);
				sumMean += sd.evaluate();
				Nsum++;
			}
		}
		double s = sumMean/Nsum;

		/*
		 * 4. Build a kd-tree
		 */
		KDTree<Point2D.Double> kdtree = new KDTree<Point2D.Double>(2);
		
		for(int i = 0; i< points.size(); i++){
			try {
				kdtree.insert(new double[]{points.get(i).x,points.get(i).y}, points.get(i));
			} catch (KeySizeException e) {
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * 5. Using the first point f in trajectory and calculate the center of mass
		 * of all points around f (radius: 3*<s>))
		 */
		List<Point2D.Double> near = null;

		Point2D.Double first = minDistancePointToLine(p1, p3, points);
		double r1 = 3*s;
		try {
			near = kdtree.nearestEuclidean(new double[]{first.x,first.y}, r1);
			
		} catch (KeySizeException e) {
			e.printStackTrace();
		} 
		
		double cx = 0;
		double cy = 0;
		for(int i = 0; i < near.size(); i++){
			cx += near.get(i).x;
			cy += near.get(i).y;
		}
		cx /= near.size();
		cy /= near.size();

		splineSupportPoints = new ArrayList<Point2D.Double>();
		splineSupportPoints.add(new Point2D.Double(cx, cy));
		
		/* 
		 * 6. The second point is determined by finding the center-of-mass of particles in the p/2 radian 
		 * section of an annulus, r1 < r < 2r1, that is directed toward the angle with the highest number 
		 * of particles within p/2 radians.
		 * 7. This second point is then used as the center of the annulus for choosing the third point, and the process is repeated (6. & 7.).
		 */
		
		/*
		 * 6.1 Find all points in the annolous
		 */
		
		
		/*
		 * 6.2 Write each point in a coordinate system centered at the center of the sphere, calculate direction and
		 * check if it in the allowed bounds
		 */
		int nCircleSegments = 100;
		double deltaRad = 2*Math.PI/nCircleSegments;
		boolean stop = false;
		int minN = 7;
		double tempr1 = r1;
		double allowedDeltaDirection = 0.3*Math.PI;
				
		while(stop==false){
			List<Point2D.Double> nearestr1 = null;
			List<Point2D.Double> nearest2xr1 = null;
			try {
				nearestr1 = kdtree.nearestEuclidean(new double[]{splineSupportPoints.get(splineSupportPoints.size()-1).x, splineSupportPoints.get(splineSupportPoints.size()-1).y},tempr1);
				nearest2xr1 = kdtree.nearestEuclidean(new double[]{splineSupportPoints.get(splineSupportPoints.size()-1).x, splineSupportPoints.get(splineSupportPoints.size()-1).y},2*tempr1);
			} catch (KeySizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nearest2xr1.removeAll(nearestr1);
			
			double lThreshRad = 0;
			double hThreshRad = Math.PI/2;
			double stopThresh = 2*Math.PI;
			if(splineSupportPoints.size()>1){
				double directionInRad = Math.atan2(splineSupportPoints.get(splineSupportPoints.size()-1).y-splineSupportPoints.get(splineSupportPoints.size()-2).y, 
						splineSupportPoints.get(splineSupportPoints.size()-1).x-splineSupportPoints.get(splineSupportPoints.size()-2).x)+Math.PI;
				lThreshRad = directionInRad - allowedDeltaDirection/2 - Math.PI/4; 
				if(lThreshRad<0){
					lThreshRad = 2*Math.PI+lThreshRad;
				}
				if(lThreshRad>2*Math.PI){
					lThreshRad = lThreshRad - 2*Math.PI;
				}
				hThreshRad = directionInRad + allowedDeltaDirection/2 + Math.PI/4; 
				if(hThreshRad<0){
					hThreshRad = 2*Math.PI+hThreshRad;
				}
				if(hThreshRad>2*Math.PI){
					hThreshRad = hThreshRad - 2*Math.PI;
				}
				stopThresh = directionInRad + allowedDeltaDirection/2 - Math.PI/4; 
				if(stopThresh>2*Math.PI){
					stopThresh = stopThresh - 2*Math.PI;
				}
				
			}
			
			double newCx=0;
			double newCy=0;
			int newCN = 0;
			int candN = 0;
			
			//Find center with highest density of points
			double lastDist = 0;
			double newDist = 0;
			do{ 
				lastDist=Math.min(Math.abs(lThreshRad-stopThresh), 2*Math.PI-Math.abs(lThreshRad-stopThresh));

				candN=0;
				double candCx =0;
				double candCy =0;
			
				for(int i = 0; i < nearest2xr1.size(); i++){
					Point2D.Double centerOfCircle = splineSupportPoints.get(splineSupportPoints.size()-1);
					Vector2d relativeToCircle = new Vector2d(nearest2xr1.get(i).x-centerOfCircle.x,nearest2xr1.get(i).y-centerOfCircle.y);
					relativeToCircle.normalize();
					double angleInRadians = Math.atan2(relativeToCircle.y, relativeToCircle.x)+Math.PI;

					if(lThreshRad<hThreshRad){
						if(angleInRadians>lThreshRad && angleInRadians < hThreshRad){
							candCx+=nearest2xr1.get(i).x;
							candCy+=nearest2xr1.get(i).y;
							candN++;
						}	
					}
					else{
						if(angleInRadians>lThreshRad || angleInRadians < hThreshRad){
							candCx+=nearest2xr1.get(i).x;
							candCy+=nearest2xr1.get(i).y;
							candN++;
						}
					}
					
								
				}
				
				if(candN>0 && candN > newCN ){
					candCx /= candN;
					candCy /= candN;
					newCx = candCx;
					newCy = candCy;
					newCN = candN;
				}
				lThreshRad += deltaRad;
				hThreshRad += deltaRad;
				if(lThreshRad>2*Math.PI){
					lThreshRad = lThreshRad - 2*Math.PI;
				}
				if(hThreshRad>2*Math.PI){
					hThreshRad = hThreshRad - 2*Math.PI;
				}
				newDist=Math.min(Math.abs(lThreshRad-stopThresh), 2*Math.PI-Math.abs(lThreshRad-stopThresh));
			
			}while((newDist-lastDist)>0);
			
			
			//Check if the new center is valid
			if(splineSupportPoints.size()>1){
				double currentDirectionInRad = Math.atan2(splineSupportPoints.get(splineSupportPoints.size()-1).y-splineSupportPoints.get(splineSupportPoints.size()-2).y, 
						splineSupportPoints.get(splineSupportPoints.size()-1).x-splineSupportPoints.get(splineSupportPoints.size()-2).x)+Math.PI;
				double candDirectionInRad = Math.atan2(newCy-splineSupportPoints.get(splineSupportPoints.size()-1).y,
						newCx-splineSupportPoints.get(splineSupportPoints.size()-1).x)+Math.PI;
				double dDir = Math.max(currentDirectionInRad, candDirectionInRad)-Math.min(currentDirectionInRad, candDirectionInRad);
				if(dDir>2*Math.PI){
					dDir = 2*Math.PI-dDir;
				}
				if(dDir>allowedDeltaDirection){

					stop = true;
				}
			}
			boolean enoughPoints = (newCN<minN);
			boolean isNormalRadius = Math.abs(tempr1-r1)<Math.pow(10, -18);
			boolean isExtendedRadius = Math.abs(tempr1-3*r1)<Math.pow(10, -18);
			
			if(enoughPoints&& isNormalRadius){
				//Not enough points, extend search radius
				tempr1 = 3*r1;
			}
			else if(enoughPoints && isExtendedRadius){
				//Despite radius extension: Not enough points!
				stop = true;
			}
			else if(stop==false){
				splineSupportPoints.add(new Point2D.Double(newCx,newCy));
				tempr1 = r1;
			}
			
		}

		//Sort
				Collections.sort(splineSupportPoints, new Comparator<Point2D.Double>() {

					public int compare(Point2D.Double o1, Point2D.Double o2) {
						if(o1.x<o2.x){
							return -1;
						}
						if(o1.x>o2.x){
							return 1;
						}
						return 0;
					}
				});
				
	
		
		//Add endpoints
		if(splineSupportPoints.size()>1){
			Vector2d start = new Vector2d(splineSupportPoints.get(0).x-splineSupportPoints.get(1).x, splineSupportPoints.get(0).y-splineSupportPoints.get(1).y);
			start.normalize();
			start.scale(r1*3);
			splineSupportPoints.add(0, new Point2D.Double(splineSupportPoints.get(0).x+start.x, splineSupportPoints.get(0).y+start.y));
			
			Vector2d end = new Vector2d(splineSupportPoints.get(splineSupportPoints.size()-1).x-splineSupportPoints.get(splineSupportPoints.size()-2).x, 
					splineSupportPoints.get(splineSupportPoints.size()-1).y-splineSupportPoints.get(splineSupportPoints.size()-2).y);
			end.normalize();
			end.scale(r1*3);
			splineSupportPoints.add(new Point2D.Double(splineSupportPoints.get(splineSupportPoints.size()-1).x+end.x, splineSupportPoints.get(splineSupportPoints.size()-1).y+end.y));
		}
		else{
			Vector2d majordir = new Vector2d(-1, 0);
			majordir.normalize();
			majordir.scale(r1*3);
			splineSupportPoints.add(0, new Point2D.Double(splineSupportPoints.get(0).x+majordir.x, splineSupportPoints.get(0).y+majordir.y));
			majordir.scale(-1);
			splineSupportPoints.add(new Point2D.Double(splineSupportPoints.get(splineSupportPoints.size()-1).x+majordir.x, splineSupportPoints.get(splineSupportPoints.size()-1).y+majordir.y));
		

		}
		
		
		
		
		//Interpolate spline
		double[] supX = new double[splineSupportPoints.size()];
		double[] supY = new double[splineSupportPoints.size()];
		for(int i = 0; i < splineSupportPoints.size(); i++){
			supX[i] = splineSupportPoints.get(i).x;
			supY[i] = splineSupportPoints.get(i).y;
		}
		
		SplineInterpolator sIinter = new SplineInterpolator();
		spline = sIinter.interpolate(supX, supY);
		
		return spline;
	}
	
	/**
	 * Please see
	 * https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Another_formula
	 * and
	 * http://mathworld.wolfram.com/Two-PointForm.html
	 * @param p1 First point on line
	 * @param p2 Second point on line
	 * @param p Point for which the distance should be calculated.
	 * @return distance of p to line defined by p1&p2
	 */
	public  double distancePointLine(Point2D.Double p1,Point2D.Double p2,Point2D.Double p){
		double d;
		double m = (p2.y-p1.y)/(p2.x-p1.x);
		
		if(Double.isInfinite(m))
		{
			
			d = Math.abs(p2.x-p.x);
		}
		else{
			double k = -1*m*p1.x+p1.y;
			d = Math.sqrt( Math.pow( (p.x+m*p.y-m*k)/(m*m+1)-p.x , 2) + Math.pow( m*( (p.x+m*p.y-m*k)/(m*m+1))+k-p.y , 2));
		}
		return d;
	}
	
	/**
	 * 
	 * @param p1 First point on line
	 * @param p2 Second point on line
	 * @param points Points in some distance to the line
	 * @return The point in points with the minimum distance to the line defined by p1 and p2
	 */
	private Point2D.Double minDistancePointToLine(Point2D.Double p1,Point2D.Double p2,List<Point2D.Double> points){
		double minDist = Double.MAX_VALUE;
		Point2D.Double minDistancePoint = null;
		for(int i = 0; i < points.size(); i++){
			double d = distancePointLine(p1, p2, points.get(i));
			
			if(d<minDist){
				minDist = d;
				minDistancePoint = points.get(i);
				
			}
		}
		return minDistancePoint;
	}
	
	public Point2D.Double minDistancePointSpline(Point2D.Double p, int nPointsPerSegment){
			double minDistance = Double.MAX_VALUE;
			Point2D.Double minDistancePoint = null;
		    int numberOfSplines = spline.getN();
		    double[] knots = spline.getKnots();
		    for(int i = 0; i < numberOfSplines; i++){
		    	double x = knots[i];
		    	double stopx = knots[i+1];
		    	double dx = (stopx-x)/nPointsPerSegment;
		    	
		    	for(int j = 0; j < nPointsPerSegment; j++){
		    		Point2D.Double candidate = new Point2D.Double(x, spline.value(x));
		    		double d = p.distance(candidate);
		    		if(d<minDistance){
		    			minDistance = d;
		    			minDistancePoint = candidate;
		    		}
		    		x += dx;
		    	}
		    	
		    }
		    return minDistancePoint;
	}
	/**
	 * 
	 * @param p1 First point on line
	 * @param p2 Second point on line
	 * @param p Point which on one side of the line
	 * @return true when it is on the left side of the line
	 */
	private  boolean isLeft(Point2D.Double p1,Point2D.Double p2,Point2D.Double p){
		return ((p2.x - p1.x)*(p.y - p1.y) - (p2.y - p1.y)*(p.x - p1.x)) > 0;
	}
	
	private  Point2D.Double projectPointToLine(Point2D.Double p1,Point2D.Double p2,Point2D.Double p){
		double m = (p2.y-p1.y)/(p2.x-p1.x);
		double b = -1*m*p1.x+p1.y;
	
		double x = (m * p.y + p.x - m * b) / (m * m + 1);
		double y = (m * m * p.y + m * p.x + b) / (m * m + 1);

		return new Point2D.Double(x, y);
	}
	
	public PolynomialSplineFunction getSpline(){
		if(spline==null){
			spline = calculateSpline();
		}
		return spline;
	}
	
	/**
	 * @return The rotated trajectory
	 */
	public Trajectory getRotatedTrajectory(){
		return rotatedTrajectory;
	}
	
	/**
	 * @return The rotation angle for the trajectory in rad.
	 */
	public double getRotationAngle(){
		return angleRotated;
	}
	
	public List<Point2D.Double> getSplineSupportPoints(){
		return splineSupportPoints;
	}
	

}
