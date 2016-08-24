package de.biomedical_imaging.traj.math;

import de.biomedical_imaging.traJ.TrajectoryUtil;
import ij.measure.CurveFitter;

public class StraightLineFit {
	
	/*
	 * Fits:
	 * y = a + bx
	 */
	double a;
	double b;
	double goodness;

	public void doFit(double[] xdata, double[] ydata){
		
		CurveFitter fitter = new CurveFitter(xdata, ydata);
		fitter.doFit(CurveFitter.STRAIGHT_LINE);
		goodness =fitter.getFitGoodness();
		a = fitter.getParams()[0];
		a = TrajectoryUtil.isZero(a)?0:a;
		b = fitter.getParams()[1];
		b = TrajectoryUtil.isZero(b)?0:b;
		if(b < 0){
			fitter = new CurveFitter(xdata, ydata);
		
			fitter.doCustomFit("y=sqrt(a*a)+sqrt(b*b)*x", new double[]{0,0}, false);
			a = Math.abs(fitter.getParams()[0]);
			b = Math.abs(fitter.getParams()[1]);
			goodness = fitter.getFitGoodness();
		}
	}
	
	public double getA(){
		return a;
	}
	
	public double getB(){
		return b;
	}
	
	public double getGoodness(){
		return goodness;
	}

}
