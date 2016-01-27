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
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectorySplineFit;

/**
 * Implements the spline curve dynamics method to estimate D and D according to:
 * [1] B. R. Long and T. Q. Vu, 
 * “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” 
 * Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
 * @author Thorsten Wagner
 *
 */
public class SplineCurveDynamicsFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	private int nSegments;
	private TrajectorySplineFit splinefit;
	private int timelag;
	/**
	 * 
	 * @param t Trajectory for calculate spline
	 * @param nSegments
	 */
	public SplineCurveDynamicsFeature(Trajectory t, int nSegments, int timelag) {
		this.t = t;
		this.nSegments = nSegments;
		this.timelag = timelag;
	}
	
	@Override
	public double[] evaluate() {

		splinefit = new TrajectorySplineFit(t,nSegments);
		PolynomialSplineFunction spline = splinefit.calculateSpline();
		Trajectory tr = splinefit.getRotatedTrajectory();
		UnivariateFunction derivative = spline.derivative();
		int N =0;
		double sumParallel = 0;
		double sumPerpendicular = 0;
		//Split each step into replacment rependicular and parallel to spline tangent
		for(int i = timelag; i < t.size(); i+=timelag){
			Point2D.Double pRef = splinefit.minDistancePointSpline(new Point2D.Double(tr.get(i).x, tr.get(i).y), 50);

			Point2D.Double pTangend = new Point2D.Double(pRef.x+1, derivative.value(pRef.x)*(pRef.x+1-pRef.x)+spline.value(pRef.x) );
			Point2D.Double pNormal = new Point2D.Double(-1*pTangend.y, pTangend.x);

			
			Point2D.Double dp = new Point2D.Double(pRef.x + tr.get(i).x-tr.get(i-timelag).x, pRef.y+ tr.get(i).y-tr.get(i-timelag).y);
			sumParallel+=Math.pow(splinefit.distancePointLine(pRef, pNormal, dp), 2);
			sumPerpendicular+=Math.pow(splinefit.distancePointLine(pRef, pTangend, dp),2);
	
			N++;
		}
		//System.out.println("N: " +spline.getN());
		double msdParallel = sumParallel/N;
		double msdPerpendicular = sumPerpendicular/N;
		result = new double[]{msdParallel,msdPerpendicular};
		return result;
	}
	
	public TrajectorySplineFit getTrajectorySplineFitInstance(){
		return splinefit;
	}
	

	@Override
	public String getName() {
		return "Spline curve dynamics";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "SCDA";
	}

}
