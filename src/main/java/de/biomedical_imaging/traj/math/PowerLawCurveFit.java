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

import ij.IJ;
import ij.measure.CurveFitter;

import com.jom.OptimizationProblem;

public class PowerLawCurveFit {
	
	private double alpha;
	private double dc;
	private double goodness;
	
	public enum FitMethod{
		SIMPLEX,JOM_CONSTRAINED
	}
	
	public PowerLawCurveFit() {
		
	}
	
	public void doFit(double[] xdata, double[] ydata, FitMethod method) {
		if(method == FitMethod.JOM_CONSTRAINED){
			
			try{
				System.loadLibrary("ipopt");
			}
			catch(UnsatisfiedLinkError e){
				String errmessage = "It seems that the IPOPT solver is not installed on your system. \n"
						+ "Please see the following website for more information: \n"
						+ "http://www.net2plan.com/jom/installation.php \n"
						+ "Pre-compiled binaries could be found here: \n"
						+ "http://www.coin-or.org/download/binary/OptimizationSuite/";
				System.err.print(errmessage);
				if(IJ.getInstance() != null){
					IJ.error(errmessage);
				}
				throw new UnsatisfiedLinkError(errmessage);
			}
		}
		doFit(xdata, ydata, method, false, 0, 0);
	}
	
	public void doFit(double[] xdata, double[] ydata, FitMethod method, double initalAlpha, double  initalDiffCoeff){
		doFit(xdata, ydata, method, true, initalAlpha, initalDiffCoeff);
	}
	
	private void doFit(double[] xdata, double[] ydata, FitMethod method, boolean useInitialGuess, double initalAlpha, double  initalDiffCoeff){
		

		switch (method) {
		case SIMPLEX:
			CurveFitter fitter = new CurveFitter(xdata, ydata);
			if(useInitialGuess){
				fitter.setInitialParameters(new double[]{initalDiffCoeff,alpha});
			}
			double init[] =null;
			if(useInitialGuess){
				init = new double[]{initalAlpha,initalDiffCoeff};
			}
			for(int i = 0; i < ydata.length; i++){
				ydata[i] = Math.log(ydata[i]);
			}
			//fitter.doFit(CurveFitter.POWER_REGRESSION);
			fitter.doCustomFit("y=sqrt(a*a)*log(x)+log(4*sqrt(b*b))", init, false);
			double params[] = fitter.getParams();
			alpha = Math.abs(params[0]);
			dc = Math.abs(params[1]); 
			goodness = fitter.getFitGoodness();
			break;
			
		case JOM_CONSTRAINED:
			
			OptimizationProblem op = new OptimizationProblem();
			op.setInputParameter("y", ydata, "column");
			op.setInputParameter("x", xdata, "column");
			op.addDecisionVariable("a", false, new int[]{1,1},0,3);
			op.addDecisionVariable("D", false, new int[]{1,1});
			op.addConstraint("a>=0");
			op.addConstraint("D<=0");
			if(useInitialGuess){
				op.setInitialSolution("a", initalAlpha);
				op.setInitialSolution("D", Math.log(initalDiffCoeff));
			}
		
			op.setObjectiveFunction("minimize", "sum( (ln(y) - (a*ln(x) + D ) )^2   )");
			
			op.solve("ipopt");
			

			if (!op.solutionIsOptimal()) {
			        System.out.println("Not optimal");
			}
			alpha = op.getPrimalSolution("a").toValue();
			dc = Math.exp(op.getPrimalSolution("D").toValue())/4;
			goodness = op.getOptimalCost();
			break;
			
		default:
			break;
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
