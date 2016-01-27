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

import ij.measure.CurveFitter;
import de.biomedical_imaging.traJ.Trajectory;

public class PowerLawFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int minlag;
	private int maxlag;
	public PowerLawFeature(Trajectory t, int minlag, int maxlag) {
		this.t = t;
		this.minlag = minlag;
		this.maxlag = maxlag;
	}
	
	@Override
	public double[] evaluate() {
		int N = maxlag - minlag + 1;
		double[] xData = new double[N];
		double[] yData = new double[N];
		for(int i = minlag; i <= maxlag; i++){
			MeanSquaredDisplacmentFeature msdFeature = new MeanSquaredDisplacmentFeature(t, i);
			xData[i-minlag] = i;
			yData[i-minlag] = msdFeature.evaluate()[0];
			//System.out.print(minlag/30.0+",");
		}
		CurveFitter fitter = new CurveFitter(xData, yData);
		double[] start = {1,1};
		fitter.setInitialParameters(start);
		fitter.doFit(CurveFitter.POWER);

		double params[] = fitter.getParams();
		
		double exponent = params[1];
		//System.out.println("0: " + params[0] + " 1: " + params[1]);
		result = new double[] {exponent};
		return result;
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
