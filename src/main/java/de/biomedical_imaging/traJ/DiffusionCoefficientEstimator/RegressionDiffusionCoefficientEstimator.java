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

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.knowm.xchart.Chart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.AbstractMeanSquaredDisplacmentEvaluator;
import de.biomedical_imaging.traJ.features.AbstractTrajectoryFeature;
import de.biomedical_imaging.traJ.features.MeanSquaredDisplacmentFeature;
import de.biomedical_imaging.traj.math.StraightLineFit;
/**
 * 
 * @author Thorsten Wagner
 */
public class RegressionDiffusionCoefficientEstimator extends AbstractTrajectoryFeature implements AbstractDiffusionCoefficientEstimator {
	private int lagMin;
	private int lagMax;
	private AbstractMeanSquaredDisplacmentEvaluator msdevaluator;
	private Trajectory t;
	private double fps;
	
	public RegressionDiffusionCoefficientEstimator(int lagMin, int lagMax) {
		this.lagMin = lagMin;
		this.lagMax = lagMax;
		msdevaluator = new MeanSquaredDisplacmentFeature(null, lagMin);
	}
	
	public RegressionDiffusionCoefficientEstimator(Trajectory t, double fps, int lagMin, int lagMax) {
		this.lagMin = lagMin;
		this.lagMax = lagMax;
		msdevaluator = new MeanSquaredDisplacmentFeature(null, lagMin);
		this.t = t;
		this.fps = fps;
	}
	
	/**
	 * @return [0] = diffusion coefficent, [1] = slope, [2] = Intercept, [3] Goodness
	 */

	public double[] getDiffusionCoefficient(Trajectory t, double fps) {
		if(t.size()==1){
			return null;
		}
		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		double msdhelp = 0;
		if(lagMin==lagMax){
			xDataList.add(0.0);
			yDataList.add(0.0);
		}
		msdevaluator.setTrajectory(t);
		msdevaluator.setTimelag(lagMin);
		
		for(int i = lagMin; i < lagMax+1; i++){
			msdevaluator.setTimelag(i);
			double[] res = msdevaluator.evaluate();
			msdhelp= res[0];
			int N = (int)res[2];
			for(int j = 0; j < N; j++){
				xDataList.add(i*1.0/fps);
				yDataList.add(msdhelp) ;
			}
		}
		double[] xdata = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] ydata = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		StraightLineFit fdf = new StraightLineFit();
		fdf.doFit(xdata, ydata);
		
		result = new double[]{fdf.getB()/(2.0*t.getDimension()),fdf.getB()*2.0*t.getDimension(),fdf.getA(), fdf.getGoodness()};
		return result;
	}
	
	public void setTimelags(int lagMin, int lagMax){
		this.lagMin = lagMin;
		this.lagMax = lagMax;
	}
	
	
	public void setMeanSquaredDisplacementEvaluator(AbstractMeanSquaredDisplacmentEvaluator msdeval){
		this.msdevaluator = msdeval;
	}

	@Override
	public double[] evaluate() {
		result = getDiffusionCoefficient(t, fps);
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Diffusion coefficient (Regression)";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "DC-REG";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}


}
