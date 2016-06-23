package de.biomedical_imaging.traJ.features;

import ij.IJ;
import ij.measure.CurveFitter;
import ij.measure.UserFunction;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import com.jom.OptimizationProblem;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.AbstractDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.CovarianceDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * Fits a function to
 * <r^2> = A [1-B*exp(-4*C*D*t/A)]
 * Where A is the squared corral size (radius), and B&C shape parameters of the corral and D the diffusion coefficient.
 * This follows the description for correlad (confined) diffusion in
 * Saxton, M.J. & Jacobson, K., 1997. Single-particle tracking: applications to membrane dynamics. 
 * Annual review of biophysics and biomolecular structure, 26, pp.373–399.
 * 
 * The class provides different fitting methods:
 *  - SIMPLEX_SINGLE uses the a simplex method where B,C are fixed to 1 and D is estimated. It only fits the corral size A. (FASTED METHOD)
 *  - SIMPLEX_COMPLETE is the same as SIMPLEX_SINGLE but B and C are not fixed. (SLOW METHOD)
 *  - JOM_CONSTRAINED try to find the optimal solution for A,B,C and D using the Java Optimization Modeler (ipopt). For this modde, you have
 *  to ipopt solver installed. Please see here for more information:
 *  
 * @author Thorsten Wagner
 *
 */
public class ConfinedDiffusionParametersFeature extends AbstractTrajectoryFeature {
	
	public enum FitMethod{
		SIMPLEX_SINGLE,SIMPLEX_COMPLETE,JOM_CONSTRAINED
	}
	
	private Trajectory t;
	private double timelag;
	private boolean onlyradius;
	private AbstractDiffusionCoefficientEstimator dcEst;
	private FitMethod fitmethod;
	

	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object. By default it uses
	 * the {@link de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator} 
	 * with min timelag 1 and maxtimelag 2  
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 */
	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag) {
		this.t = t;
		this.timelag = timelag;
		onlyradius = false;
		dcEst = new RegressionDiffusionCoefficientEstimator(null, 1/timelag, 1, 2);
		fitmethod = FitMethod.SIMPLEX_SINGLE;
	}
	
	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object.
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 * @param dcEst Estimateor for the diffusion coefficient.
	 */
	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag, AbstractDiffusionCoefficientEstimator dcEst, FitMethod fitmethod) {
		this.t = t;
		this.timelag = timelag;
		onlyradius = false;
		this.dcEst = dcEst;
		this.fitmethod = fitmethod;
	}
	
	@Override
	/**
	 * @return [0] = squared radius, [1] = shape parameter 1, [2] shape parameter 2, [3] Fit goodness.
	 * When onlyRadius==true then [0] = squared radius, [1] Fit goodness
	 */
	public double[] evaluate() {
		CovarianceDiffusionCoefficientEstimator ce = new CovarianceDiffusionCoefficientEstimator(t, 1/timelag);
		IJ.log("EstDC: " + ce.evaluate()[0]);
		MeanSquaredDisplacmentFeature msd = new MeanSquaredDisplacmentFeature(t, 1);
		msd.setOverlap(false);

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		
		for(int i = 1; i < t.size()/3; i++){
			msd.setTimelag(i);
			double[] res = msd.evaluate();
			double msdvalue = res[0];
			int N = (int)Math.sqrt(res[2]); 
			for(int j = 0; j < N; j++){
				xDataList.add((double) i*timelag);
				yDataList.add(msdvalue);
			}
		}
		double[] xData = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] yData = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		CurveFitter fitter = new CurveFitter(xData, yData);
		MaxDistanceBetweenTwoPositionsFeature maxdist = new MaxDistanceBetweenTwoPositionsFeature(t);
		double estdia = maxdist.evaluate()[0];
		
		double estDC = dcEst.getDiffusionCoefficient(t, 1/timelag)[0];
	
		double[] res = null;
		double[] initialParams = null;
		double[] params = null;
		
		switch (fitmethod) {
		
		case SIMPLEX_SINGLE:
			initialParams = new double[]{estdia*estdia};//,regest.evaluate()[0]};
			fitter.doCustomFit("y=a*(1-exp(-4*"+estDC+"*x/a))", initialParams, false);
			 params = fitter.getParams();
			res = new double[]{params[0],fitter.getFitGoodness()};
			
			break;
		case SIMPLEX_COMPLETE:
			initialParams = new double[]{estdia*estdia,1,1};//,regest.evaluate()[0]};
			fitter.doCustomFit("y=a*(1-b*exp(-4*c*"+estDC+"*x/a))", initialParams, false);
			params = fitter.getParams();
			res = new double[]{params[0],params[1],params[2],fitter.getFitGoodness()};
			
			break;
		case JOM_CONSTRAINED:
			OptimizationProblem op = new OptimizationProblem();
			
			op.addDecisionVariable("a", false, new int[]{1,1},0,estdia*estdia);
			op.addDecisionVariable("b", false, new int[]{1,1});
			op.addDecisionVariable("c", false, new int[]{1,1});
			op.addDecisionVariable("D", false, new int[]{1,1},0,estDC*2);
			op.setInputParameter("y", yData, "column");
			op.setInputParameter("x", xData, "column");
		
		
			op.setInitialSolution("a", estdia*estdia);
			op.setInitialSolution("b", 1);
			op.setInitialSolution("c", 1);
			op.setInitialSolution("D", estDC);
			op.setObjectiveFunction("minimize", "sum( (y - a*(1-b*exp((-4*D)*(x/a)*c)))^2   )");
			op.addConstraint("a>=0");
			op.addConstraint("b>=0");
			op.addConstraint("c>=0");
			op.addConstraint("D>=0");
			/**
			 * TODO: Check if ipopt is installed
			 */
			op.solve("ipopt");

			if (!op.solutionIsOptimal()) {
			        System.out.println("Not optimal");
			}
			double a = op.getPrimalSolution("a").toValue();
			double b = op.getPrimalSolution("b").toValue();
			double c = op.getPrimalSolution("c").toValue();
			double D = op.getPrimalSolution("D").toValue();
	
			res = new double[]{a,D,b,c};
			break;

		default:
			break;
		}
		
		return res;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Confinement Parameters";
	}
	
	public void setFitMethod(FitMethod fitmethod){
		this.fitmethod = fitmethod;
	}
	
	public FitMethod getFitMethod(){
		return fitmethod;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "CONFPARAM";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
