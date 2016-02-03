/*
The MIT License (MIT)

Copyright (c) 2015 Thorsten Wagner (wagner@biomedical-imaging.de)

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

package de.biomedical_imaging.traJ.DiffusionCoefficientEstimator;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.knowm.xchart.Chart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.AbstractMeanSquaredDisplacmentEvaluator;
import de.biomedical_imaging.traJ.features.AbstractTrajectoryFeature;
import de.biomedical_imaging.traJ.features.MeanSquaredDisplacmentFeature;
/**
 * 
 * @author Thorsten Wagner
 *
 */
public class RegressionDiffusionCoefficientEstimator implements AbstractDiffusionCoefficientEstimator {
	private int lagMin;
	private int lagMax;
	private AbstractMeanSquaredDisplacmentEvaluator msdevaluator;
	
	public RegressionDiffusionCoefficientEstimator(int lagMin, int lagMax) {
		this.lagMin = lagMin;
		this.lagMax = lagMax;
		msdevaluator = new MeanSquaredDisplacmentFeature(null, lagMin);
	}
	
	/**
	 * @return [0] = diffusion coefficent, [2] = slope, [3] = Intercept
	 */

	public double[] getDiffusionCoefficient(Trajectory t, double fps) {
		if(t.size()==1){
			return null;
		}
		SimpleRegression reg = new SimpleRegression(true);
		double msdhelp = 0;
		if(lagMin==lagMax){
			reg.addData(0, 0);
		}
		msdevaluator.setTrajectory(t);
		msdevaluator.setTimelag(lagMin);
		for(int i = lagMin; i < lagMax+1; i++){
			msdevaluator.setTimelag(i);
			msdhelp= msdevaluator.evaluate()[0];
			reg.addData(i*1.0/fps, msdhelp);
		}
		double[] D = {reg.getSlope()/(2.0*t.getDimension()),reg.getSlope(),reg.getIntercept()}; 
		return D;
	}
	
	public void setTimelags(int lagMin, int lagMax){
		this.lagMin = lagMin;
		this.lagMax = lagMax;
	}
	
	public static  void plotMSDLine(Trajectory t, int lagMin, int lagMax, AbstractMeanSquaredDisplacmentEvaluator msdeval){
		
	 	double[] xData = new double[lagMax-lagMin+1];
	    double[] yData = new double[lagMax-lagMin+1];
	    msdeval.setTrajectory(t);
	    msdeval.setTimelag(lagMin);
		for(int i = lagMin; i < lagMax+1; i++){
			msdeval.setTimelag(i);
			double msdhelp= msdeval.evaluate()[1];
			xData[i-lagMin] = i;
	    	yData[i-lagMin] = msdhelp;
		}
	 
	    // Create Chart
	    Chart chart = QuickChart.getChart("MSD Line", "LAG", "MSD", "MSD", xData, yData);
	    
	    //Show it
	    new SwingWrapper(chart).displayChart();
}
	
	public static  void plotMSDLine(Trajectory t, int lagMin, int lagMax){
			plotMSDLine(t, lagMin, lagMax, new MeanSquaredDisplacmentFeature(t, lagMin));
	}
	
	public void setMeanSquaredDisplacementEvaluator(AbstractMeanSquaredDisplacmentEvaluator msdeval){
		this.msdevaluator = msdeval;
	}


}
