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

/**
 * Fits the following model: y = a*(1-b*exp((-4*D)*(x/a)*c)) whereas 
 * a is the corral size
 * b&c are depending on the shape of the confinement and 
 * D is the diffusion coefficient
 * 
 * a,b,c and D are constrained to positive values!
 * @author Thorsten Wagner
 *
 */
public class ConfinedDiffusionMSDCurveFit {
	
	private double a;
	private double b;
	private double c;
	private double D;
	private double goodness;
	private double initA;
	private double initB;
	private double initC;
	private double initD;
	
	public ConfinedDiffusionMSDCurveFit() {
		initA = Double.NaN;
		initB = Double.NaN;
		initC = Double.NaN;
		initD = Double.NaN;;
	}
	
	/**
	 * Fits the curve y = a*(1-b*exp((-4*D)*(x/a)*c)) to the x- and y data.
	 * The parameters have the follow meaning:
	 * 
	 * @param xdata
	 * @param ydata
	 */
	public void doFit(double[] xdata, double[] ydata){
			CurveFitter fitter = new CurveFitter(xdata, ydata);
			double ia = Double.isNaN(initA)?0:initA;
			double ib = Double.isNaN(initB)?0:initB;
			double ic = Double.isNaN(initC)?0:initC;
			double id = Double.isNaN(initD)?0:initD;
			double[] initialParams = new double[]{ia,ib,ic,id};//,regest.evaluate()[0]};
			fitter.setInitialParameters(initialParams);
			//fitter.doCustomFit("y=a*(1-b*exp(-4*c*d*x/a))", initialParams, false);
			fitter.doCustomFit("y=sqrt(a*a)*(1-sqrt(b*b)*exp(-4*sqrt(c*c)*sqrt(d*d)*x/sqrt(a*a)))", initialParams, false);
			double[] params = fitter.getParams();
			a = Math.abs(params[0]);
			b = Math.abs(params[1]);
			c = Math.abs(params[2]);
			D = Math.abs(params[3]);
			goodness = fitter.getFitGoodness();
	}
	
	public void setInitParameters(double[] p){
		initA = p[0];
		initB = p[1];
		initC = p[2];
		initD = p[3];
	}
	
	public double getA(){
		return a;
	}
	
	public double getB(){
		return b;
	}
	
	public double getC(){
		return c;
	}
	
	public double getD(){
		return D;
	}
	
	public double getGoodness(){
		return goodness;
	}

}
