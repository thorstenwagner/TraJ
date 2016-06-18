package de.biomedical_imaging.traJ.features;

import ij.measure.CurveFitter;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.AbstractDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * Fits a function to
 * <r^2> = A [1-B*exp(-4*C*D*t/A)]
 * Where A is the squared corral size (radius), and B&C shape parameters of the corral and D the diffusion coefficient.
 * This follows the description for correlad (confined) diffusion in
 * Saxton, M.J. & Jacobson, K., 1997. Single-particle tracking: applications to membrane dynamics. 
 * Annual review of biophysics and biomolecular structure, 26, pp.373–399.
 * @author Thorsten Wagner
 *
 */
public class ConfinedDiffusionParametersFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private double timelag;
	private boolean onlyradius;
	private AbstractDiffusionCoefficientEstimator dcEst;

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
	}
	
	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object.
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 * @param dcEst Estimateor for the diffusion coefficient.
	 */
	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag, AbstractDiffusionCoefficientEstimator dcEst) {
		this.t = t;
		this.timelag = timelag;
		onlyradius = false;
		this.dcEst = dcEst;
	}
	
	@Override
	/**
	 * @return [0] = squared radius, [1] = shape parameter 1, [2] shape parameter 2, [3] Fit goodness.
	 * When onlyRadius==true then [0] = squared radius, [1] Fit goodness
	 */
	public double[] evaluate() {
		MeanSquaredDisplacmentFeature msd = new MeanSquaredDisplacmentFeature(t, 1);
		msd.setOverlap(false);

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		for(int i = 1; i < t.size(); i++){
			msd.setTimelag(i);
			double[] res = msd.evaluate();
			double msdvalue = res[0];
			int N = (int)res[2];
			for(int j = 0; j < N; j++){
				xDataList.add((double) i*timelag);
				yDataList.add(msdvalue);
			}
		}
		double[] xData = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] yData = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		CurveFitter fitter = new CurveFitter(xData, yData);
		MaxDistanceBetweenTwoPositionsFeature maxdist = new MaxDistanceBetweenTwoPositionsFeature(t);
		double estrad = maxdist.evaluate()[0];
		double estDC = dcEst.getDiffusionCoefficient(t, 1/timelag)[0];
		double[] res = null;
		if(onlyradius){
			double[] initialParams = {estrad*estrad};//,regest.evaluate()[0]};
			fitter.doCustomFit("y=a*(1-exp(-4*"+estDC+"*x/a))", initialParams, false);
			double[] params = fitter.getParams();
			res = new double[]{params[0],fitter.getFitGoodness()};
		}else{
			double[] initialParams = {estrad*estrad,1,1};//,regest.evaluate()[0]};
			fitter.doCustomFit("y=a*(1-b*exp(-4*c*"+estDC+"*x/a))", initialParams, false);
			double[] params = fitter.getParams();
			res = new double[]{params[0],params[1],params[2],fitter.getFitGoodness()};
		}
		return res;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Confinement Parameters";
	}
	
	/**
	 * 
	 * @param or True, the only the parameter A is estimated (A could be interpreted as the squared radius of the confinement).
	 */
	public void setOnlyRadius(boolean or){
		onlyradius = or;
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
