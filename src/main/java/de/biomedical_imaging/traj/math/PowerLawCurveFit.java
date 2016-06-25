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
	
	public void doFit(double[] xdata, double[] ydata, FitMethod method,double initalAlpha, double  initalDiffCoeff){
		doFit(xdata, ydata, method, initalAlpha, initalDiffCoeff);
	}
	
	private void doFit(double[] xdata, double[] ydata, FitMethod method, boolean useInitialGuess, double initalAlpha, double  initalDiffCoeff){
		

		switch (method) {
		case SIMPLEX:
			CurveFitter fitter = new CurveFitter(xdata, ydata);
			if(useInitialGuess){
				fitter.setInitialParameters(new double[]{initalDiffCoeff,alpha});
			}
			fitter.doFit(CurveFitter.POWER_REGRESSION);

			double params[] = fitter.getParams();
			alpha = params[1];
			dc = params[0]/4; 
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
