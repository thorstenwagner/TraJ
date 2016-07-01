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
		catch(EmptyStackException e){
			return new double[] {Double.POSITIVE_INFINITY};
		}

		double longSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[0].distance(rect[1]):rect[1].distance(rect[2]);
		double shortSide = rect[0].distance(rect[1])>rect[1].distance(rect[2])?rect[1].distance(rect[2]):rect[0].distance(rect[1]);
		result = new double[] {longSide/shortSide};
		return result;
	}

	@Override
	public String getName() {
		return "Abstract ratio";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}
	@Override
	public String getShortName() {
		return "AR";
	}

}
