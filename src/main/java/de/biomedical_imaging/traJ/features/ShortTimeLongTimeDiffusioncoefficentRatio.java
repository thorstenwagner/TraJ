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
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;

/**
 * The ratio of the long time diffusion coefficent and the short  time diffusion coefficient
 * @author thorsten
 *
 */
public class ShortTimeLongTimeDiffusioncoefficentRatio extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int numberOfPoints;
	
	public ShortTimeLongTimeDiffusioncoefficentRatio(Trajectory t, int numberOfPoints) {
		this.t = t;
		this.numberOfPoints = numberOfPoints;
	}
	
	@Override
	public double[] evaluate() {
		RegressionDiffusionCoefficientEstimator rgShort = new RegressionDiffusionCoefficientEstimator(1, 1+numberOfPoints);
		RegressionDiffusionCoefficientEstimator rgLong = new RegressionDiffusionCoefficientEstimator(t.size()/10 - numberOfPoints, t.size()/10);
		double Dshort = rgShort.getDiffusionCoefficient(t, 1)[0];
		double Dlong = rgLong.getDiffusionCoefficient(t, 1)[0];
		result = new double[]{Dlong/Dshort};
		return result;
	}

	@Override
	public String getName() {
		
		return "Short time / long time diffusion coefficient ratio";
	}

	@Override
	public String getShortName() {
		return "LTST.RATIO";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
