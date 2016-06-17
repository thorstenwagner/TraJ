package de.biomedical_imaging.traJ.features;

import ij.measure.CurveFitter;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import de.biomedical_imaging.traJ.Trajectory;
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

	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	/**
	 * @return [0] = squared radius, [1] = shape parameter 1, [2] shape parameter 2
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
		RegressionDiffusionCoefficientEstimator regest = new RegressionDiffusionCoefficientEstimator(t, 1/timelag, 1, 2);
		double estrad = maxdist.evaluate()[0];
		double estDC = regest.evaluate()[0];
		double[] initialParams = {estrad*estrad,1,1};//,regest.evaluate()[0]};
		fitter.doCustomFit("y=a*(1-b*exp(-4*c*"+estDC+"*x/a))", initialParams, false);
		double[] params = fitter.getParams();
		double[] res = {params[0],params[1],params[2],fitter.getFitGoodness()};
		return res;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Confinement Parameters";
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
