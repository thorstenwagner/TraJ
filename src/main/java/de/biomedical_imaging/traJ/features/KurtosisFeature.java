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
import de.biomedical_imaging.traj.math.MomentsCalculator;

/**
 * Evaluates the skewness of the trajectory. Therefore the radius of gyration tensor is estimated.
 * For the calculation, the positions are projected on the dominant eigenvector of the radius of gyration
 * tensor.:
 * Skewness = 1/N sum( (x_i - mean)^3 / sd^3)
 * where N is the number of positions, mean the mean position and sd the standard deviation
 * @author Thorsten Wagner
 *
 */
public class KurtosisFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public KurtosisFeature(Trajectory t) {
		this.t = t;
	}
	
	
	@Override
	public double[] evaluate() {
		MomentsCalculator moments = new MomentsCalculator(t);
		result = new double[] {moments.calculateNthMoment(4)};
		return result;
	}

	@Override
	public String getName() {
		return "Kurtosis";
	}

	@Override
	public String getShortName() {
		return "KURT";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t =t ;
		result = null;
		
	}

}
