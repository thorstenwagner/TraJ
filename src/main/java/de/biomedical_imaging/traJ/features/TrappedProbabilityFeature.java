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

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.CovarianceDiffusionCoefficientEstimator;

/**
 * Following Saxton [1], the probability p that a trajectory is trapped
 * inside a radius R is:
 *   p = 1 - exp(0.2048-2.5117(D*t/R^2))
 * where D is the diffusion coefficient, and t is time duration of the trajectory.
 * 
 * For this feature R is estiamted by the maximum distance between two positions and
 * D is estimated by the the covariance estimator.
 * @author thorsten
 *
 */
public class TrappedProbabilityFeature extends AbstractTrajectoryFeature{
	private Trajectory t;
	
	/**
	 * 
	 * @param t Trajectory
	 * @param timelag Timelag between two steps
	 */
	public TrappedProbabilityFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		MaxDistanceBetweenTwoPositionsFeature dtwop = new MaxDistanceBetweenTwoPositionsFeature(t);
		double r = dtwop.evaluate()[0]/2;
		
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator(t, 1);
		
		double D = dcEst.evaluate()[0];
		double time = t.size();

		double p = 1- Math.exp(0.2048-2.5117*(D*time/(r*r)));
		result = new double[]{p};
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Trapped trajectory probability";
	}

	@Override
	public String getShortName() {
		return "TRAPPED";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
