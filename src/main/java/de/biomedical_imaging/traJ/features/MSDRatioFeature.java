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
 * Calculates ratio of the MSD for two give timelags.
 * 
 * @author Thorsten Wagner
 *
 */
public class MSDRatioFeature  extends AbstractTrajectoryFeature {
	private int timelag1;
	private int timelag2;
	private Trajectory t;
	/**
	 * 
	 * @param t Trajectory for which the MSD ratio is to be calculated
	 * @param timelag1 Timelag for the numerator MSD
	 * @param timelag2 Timelag for the denominator MSD
	 */
	public MSDRatioFeature(Trajectory t, int timelag1, int timelag2) {
		this.t = t;
		this.timelag1 = timelag1;
		this.timelag2 = timelag2;
	}
	/**
	 * @return An double array with the elements [0] = MSD Ratio
	 */
	@Override
	public double[] evaluate() {
		MeanSquaredDisplacmentFeature msdf1 = new MeanSquaredDisplacmentFeature(t, timelag1);
		MeanSquaredDisplacmentFeature msdf2 = new MeanSquaredDisplacmentFeature(t, timelag2);

		double msd1 = msdf1.evaluate()[0];
		double msd2 = msdf2.evaluate()[0];
		double res = (msd1)/(msd2) - 1.0*timelag1/timelag2;
		result = new double[]{res};
		return result;
	}

	@Override
	public String getName() {
		return "Mean squared displacment ratio";
	}

	@Override
	public String getShortName() {
		return "MSDR";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
