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

package de.biomedical_imaging.traJ.features;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.EmptyStackException;

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
	
	/**
	 * 
	 * @param t Trajectory for which the elongation is to be calcualted.
	 */
	public ElongationFeature(Trajectory t) {
		this.t = t;
	}
	
	/**
	 * @return Returns an double array with the elements [0]=Elongation
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
			return new double[] {1}; //For colinear points it is defined as 1
		}
		catch(EmptyStackException e){
			return new double[] {1}; 
		}

		double longSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[0].distance(rect[1]):rect[1].distance(rect[2]);
		double shortSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[1].distance(rect[2]):rect[0].distance(rect[1]);
		result = new double[] {1-shortSide/longSide};
		return result;
	}

	@Override
	public String getName() {
		
		return "Elongation";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

	@Override
	public String getShortName() {
		
		return "ELONG";
	}

}
