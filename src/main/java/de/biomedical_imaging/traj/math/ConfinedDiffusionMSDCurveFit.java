package de.biomedical_imaging.traj.math;

import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;

import ij.IJ;
import ij.measure.CurveFitter;

import com.jom.OptimizationProblem;

import de.biomedical_imaging.traj.math.PowerLawCurveFit.FitMethod;

public class ConfinedDiffusionMSDCurveFit {
	
	private double a;
	private double b;
	private double c;
	private double D;
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
	
	public enum FitMethod{
		SIMPLEX,JOM_CONSTRAINED
	}
	
	/**
	 * Fits the curve y = a*(1-b*exp((-4*D)*(x/a)*c)) to the x- and y data.
	 * The parameters have the follow meaning:
	 * 
	 * @param xdata
	 * @param ydata
	 * @param method
	 */
	public void doFit(double[] xdata, double[] ydata, FitMethod method){
		if(method == FitMethod.JOM_CONSTRAINED){
			
			try{
				System.loadLibrary("ipopt");
			}
			catch(UnsatisfiedLinkError e){
				String errmessage = "It seems that the IPOPT solver is not installed on your system. \n"
						+ "Linux users will find easy to install packages with their package manager."
						+ "Please see the following website for more information: \n"
						+ "http://www.net2plan.com/jom/installation.php \n";
				System.err.print(errmessage);
				if(IJ.getInstance() != null){
					IJ.error(errmessage);
				}
				throw new UnsatisfiedLinkError(errmessage);
			}
		}
		
		switch (method) {
		case SIMPLEX:
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
			break;
		case JOM_CONSTRAINED:
			OptimizationProblem op = new OptimizationProblem();
	
			op.addDecisionVariable("a", false, new int[]{1,1});//,0,initA);
			op.addDecisionVariable("b", false, new int[]{1,1});//,0,1);
			op.addDecisionVariable("c", false, new int[]{1,1});//,0,1);
			op.addDecisionVariable("D", false, new int[]{1,1});//,0,initD*2); //,0,estDC*2
			op.setInputParameter("y", ydata, "column");
			op.setInputParameter("x", xdata, "column");
			
			op.addConstraint("a>=0");
		//	op.addConstraint("a<="+initA);
			
			op.addConstraint("b>=0");
			op.addConstraint("c>=0");
			op.addConstraint("D>=0");
			
			//op.addConstraint("D<="+initD*2);
			
			if(initA!=Double.NaN)op.setInitialSolution("a", initA);
			if(initB!=Double.NaN)op.setInitialSolution("b", initB);
			if(initC!=Double.NaN)op.setInitialSolution("c", initC);
			if(initD!=Double.NaN)op.setInitialSolution("D", initD);
			
			op.setObjectiveFunction("minimize", "sum( (y - a*(1-b*exp((-4*D)*(x/a)*c)))^2   )");
			
			op.solve("ipopt");
			
			if (!op.solutionIsOptimal()) {
		        System.out.println("Not optimal");
		}
			a = op.getPrimalSolution("a").toValue();
			b = op.getPrimalSolution("b").toValue();
			c = op.getPrimalSolution("c").toValue();
			D = op.getPrimalSolution("D").toValue();
			break;

		default:
			break;
		}	
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

}
