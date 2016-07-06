/*
 * The MIT License (MIT)
 * Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.biomedical_imaging.traj.math;

import ij.measure.CurveFitter;


public class PowerLawCurveFit {
	
	private double alpha;
	private double dc;
	private double goodness;

	public PowerLawCurveFit() {
		
	}
	
	public void doFit(double[] xdata, double[] ydata) {
		doFit(xdata, ydata, false, 0, 0);
	}
	
	public void doFit(double[] xdata, double[] ydata, double initalAlpha, double  initalDiffCoeff){
		doFit(xdata, ydata, true, initalAlpha, initalDiffCoeff);
	}
	
	private void doFit(double[] xdata, double[] ydata, boolean useInitialGuess, double initalAlpha, double  initalDiffCoeff){
			CurveFitter fitter = new CurveFitter(xdata, ydata);
			if(useInitialGuess){
				fitter.setInitialParameters(new double[]{initalDiffCoeff,alpha});
			}
			double init[] =null;
			if(useInitialGuess){
				init = new double[]{initalAlpha,initalDiffCoeff};
			}
			fitter.doFit(CurveFitter.POWER_REGRESSION);
			double params[] = fitter.getParams();
			alpha = params[1];
			dc = params[0]/4.0; 
			goodness = fitter.getFitGoodness();
		
			if(alpha < 0 || dc < 0){
		
				fitter = new CurveFitter(xdata, ydata);
				for(int i = 0; i < ydata.length; i++){
					ydata[i] = Math.log(ydata[i]);
				}
				//fitter.doFit(CurveFitter.POWER_REGRESSION);
				fitter.doCustomFit("y=sqrt(a*a)*log(x)+log(4*sqrt(b*b))", init, false);
				params = fitter.getParams();
				alpha = Math.abs(params[0]);
				dc = Math.abs(params[1]); 
				goodness = fitter.getFitGoodness();
			}
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public double getDiffusionCoefficient(){
		return dc;
	}
	
	public double getGoodness(){
		return goodness;
	}

}
