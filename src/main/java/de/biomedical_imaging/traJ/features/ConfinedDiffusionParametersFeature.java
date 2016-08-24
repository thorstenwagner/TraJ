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

package de.biomedical_imaging.traJ.features;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.AbstractDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;
import de.biomedical_imaging.traj.math.ConfinedDiffusionMSDCurveFit;

/**
 * Fits a function to
 * <r^2> = A [1-B*exp(-4*C*D*t/A)]
 * Where A is the squared corral size (radius), and B&C shape parameters of the corral and D the diffusion coefficient. A, B, C and D 
 * are restricted to be positive.
 * This follows the description for correlad (confined) diffusion in
 * Saxton, M.J. & Jacobson, K., 1997. Single-particle tracking: applications to membrane dynamics. 
 * Annual review of biophysics and biomolecular structure, 26, pp.373–399.
 *  
 * @author Thorsten Wagner
 *
 */
public class ConfinedDiffusionParametersFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	private double timelag;
	private AbstractDiffusionCoefficientEstimator dcEst;
	private boolean useReducedModel;
	

	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object. By default it uses
	 * the {@link de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator} 
	 * with min timelag 1 and maxtimelag 2  
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 */
	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag, boolean useReducedModel) {
		this.t = t;
		this.timelag = timelag;
		dcEst = new RegressionDiffusionCoefficientEstimator(null, 1/timelag, 1, 2);
		this.useReducedModel = useReducedModel;
	}
	
	/**
	 * Constructs a newly allocated ConfinedDiffusionParametersFeature object.
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 * @param dcEst Estimateor for the diffusion coefficient.
	 */
	public ConfinedDiffusionParametersFeature(Trajectory t, double timelag, boolean useReducedModel, AbstractDiffusionCoefficientEstimator dcEst) {
		this.t = t;
		this.timelag = timelag;
		this.dcEst = dcEst;
		this.useReducedModel = useReducedModel;
	}
	
	@Override
	/**
	 * @return Returns an double array with the elements [0] = squared radius (A), [1] = Diffusion coefficent (D) [2] = shape parameter 1 (B), 
	 * [3] shape parameter 2 (C) and  [4] Fit goodness. If reduced model was selected then it return [0] = squared radius, [1] = diffusion coefficient, [3] = goodness
	 */
	public double[] evaluate() {
		MeanSquaredDisplacmentFeature msd = new MeanSquaredDisplacmentFeature(t, 1);
		msd.setOverlap(false);

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		
		for(int i = 1; i < t.size()/3; i++){
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
		
		/*
		 * Estimate inital values
		 */
		MaxDistanceBetweenTwoPositionsFeature maxdist = new MaxDistanceBetweenTwoPositionsFeature(t);
		double estdia = maxdist.evaluate()[0];
		double estDC = dcEst.getDiffusionCoefficient(t, 1/timelag)[0];

		double[] initialParams = new double[]{estdia*estdia,0,0,estDC};
		
		/*
		 * Do the fit and report the results
		 */
		
		ConfinedDiffusionMSDCurveFit cmsdfit = new ConfinedDiffusionMSDCurveFit();
		cmsdfit.setInitParameters(initialParams);
		cmsdfit.doFit(xData, yData,useReducedModel);
		if(useReducedModel){
			result = new double[]{cmsdfit.getA(),cmsdfit.getD(),cmsdfit.getGoodness()};
		}else{
			result = new double[]{cmsdfit.getA(),cmsdfit.getD(),cmsdfit.getB(),cmsdfit.getC(),cmsdfit.getGoodness()};
		}
	
		return result;
	}
	

	@Override
	public String getName() {
		return "Confinement Parameters";
	}

	@Override
	public String getShortName() {
		return "CONFPARAM";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
	}

}
