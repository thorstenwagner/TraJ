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

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import ij.measure.CurveFitter;
import de.biomedical_imaging.traJ.Trajectory;

/**
 * Fits a power law curve to the msd data and returns the exponent
 * @author Thorsten Wagner
 *
 */
public class PowerLawFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int minlag;
	private int maxlag;
	private AbstractMeanSquaredDisplacmentEvaluator msdeval;
	private int evaluateIndex = 0;
	public PowerLawFeature(Trajectory t, int minlag, int maxlag) {
		this.t = t;
		this.minlag = minlag;
		this.maxlag = maxlag;
		msdeval = new MeanSquaredDisplacmentFeature(null, 0);
	
		evaluateIndex = 0;
	}
	
	@Override
	public double[] evaluate() {

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		msdeval.setTrajectory(t);
		double[][] data = new double[maxlag-minlag+1][3];

		for(int i = minlag; i <= maxlag; i++){
			msdeval.setTimelag(i);
			data[i-minlag][0] = i;
			double[] res = msdeval.evaluate();
			data[i-minlag][1] = res[evaluateIndex];
			data[i-minlag][2] = (int)res[2];
	

		}

		//Weightening
		for(int i = 0; i < (maxlag-minlag+1); i++){
			int x = (int)data[i][0];
			double y = data[i][1];
			int np = (int)data[i][2];
			for(int j = 0; j < np; j++){
				xDataList.add((double)x);
				yDataList.add(y);
			}
		}
		
		double[] xData = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] yData = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		CurveFitter fitter = new CurveFitter(xData, yData);
		//double[] start = {1,1};
	//	fitter.setInitialParameters(start);
		fitter.doFit(CurveFitter.POWER);


		double params[] = fitter.getParams();
		
		double exponent = params[1];
		//System.out.println("0: " + params[0] + " 1: " + params[1]);
		result = new double[] {exponent};
		return result;
	}
	
	public void setEvaluateIndex(int evaluateIndex){
		this.evaluateIndex = evaluateIndex;
	}
	
	public void setMeanSquaredDisplacmentEvaluator(AbstractMeanSquaredDisplacmentEvaluator msdeval){
		this.msdeval = msdeval;
	}

	@Override
	public String getName() {
		
		return "Power-Law-Feature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "POWER";
	}

}
