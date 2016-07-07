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

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * Implements the msd curvature feature according
 * S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, 
 * “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” 
 * Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.
 * @author Thorsten Wagner
 *
 */
public class MeanSquaredDisplacmentCurvature extends AbstractTrajectoryFeature {
	private Trajectory t;
	
	/**
	 * 
	 * @param t Trajectory for which the MSD curvature is to be calculated.
	 */
	public MeanSquaredDisplacmentCurvature(Trajectory t){
		this.t = t;
		result = null;
	}
	
	/**
	 * @return An double array with the elements [0] MSD curvature
	 */
	@Override
	public double[] evaluate() {
		
		
		int Ndiff = 2;
		if((int)(t.size()*0.05)>2){
			Ndiff = (int)(t.size()*0.05);
		}
		RegressionDiffusionCoefficientEstimator regEst = new RegressionDiffusionCoefficientEstimator(1, Ndiff);
		double[] regline = regEst.getDiffusionCoefficient(t, 1);
		double slope = regline[1];
		double intercept = regline[2];
		
		int Ndef = 10*Ndiff;
		if(Ndef>t.size()/2){
			Ndef = t.size()/2;
		}
		MeanSquaredDisplacmentFeature msd = new MeanSquaredDisplacmentFeature(t, 1);
		double sum = 0;
		for(int i = 1; i <= Ndef; i++){
			
			msd.setTimelag(i);
			sum+= (msd.evaluate()[0] - evaluateMSDLine(i, slope, intercept))/evaluateMSDLine(i, slope, intercept);
		}
		double dev = sum/Ndef;
		result = new double[] {dev};
		return result;
	}
	
	private double evaluateMSDLine(double v, double slope, double intercept){
		return v*slope + intercept;
	}

	@Override
	public String getName() {
		return "Mean Squared Displacment Curvature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
	}

	@Override
	public String getShortName() {
		return "MSDCURV";
	}

	
}
