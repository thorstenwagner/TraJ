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

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.TrajectorySplineFit;
/**
 * Implements the spline curve spatial analysis method according to:
 * [1] B. R. Long and T. Q. Vu, 
 * “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” 
 * Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
 * @author Thorsten Wagner
 *
 */
public class SplineCurveSpatialFeature extends AbstractTrajectoryFeature{
	private Trajectory t;
	private int nSegments;
	private TrajectorySplineFit splinefit;

	public SplineCurveSpatialFeature(Trajectory t, int nSegments) {
		this.t =t;
		this.nSegments = nSegments;
	}
	
	@Override
	/**
	 * @return [0] Mean  distance [1] SD  distance 
	 */
	public double[] evaluate() {
		splinefit = new TrajectorySplineFit(t,nSegments);
		splinefit.calculateSpline();
		if(!splinefit.wasSuccessfull()){
			return new double[] {Double.NaN,Double.NaN};
		}
		double[] data = new double[t.size()];
		for(int i = 0; i < t.size(); i++){
			
			Point2D.Double help = new Point2D.Double(splinefit.getRotatedTrajectory().get(i).x, splinefit.getRotatedTrajectory().get(i).y);
			data[i] = help.distance(splinefit.minDistancePointSpline(new Point2D.Double(splinefit.getRotatedTrajectory().get(i).x, splinefit.getRotatedTrajectory().get(i).y), 50));
		}
		Mean m = new Mean();
		StandardDeviation sd = new StandardDeviation();
		result = new double[] {m.evaluate(data),sd.evaluate(data)};
		return result;
	
		 
	}
	
	public TrajectorySplineFit getTrajectorySplineFitInstance(){
		return splinefit;
	}

	@Override
	public String getName() {
		return "Spline curve spatial feature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result=null;
		
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "SCSA";
	}


}
