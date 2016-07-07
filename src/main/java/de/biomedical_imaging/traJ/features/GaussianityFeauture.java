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

/**
 * Implements the feature "Gaussianity" as descriped in 
 * Ernst, D., Köhler, J. & Weiss, M., 2014. Probing the 
 * type of anomalous diffusion with single-particle tracking. 
 * Physical chemistry chemical physics : PCCP, 16(17), pp.7686–91.
 * 
 * @author Thorsten Wagner 
 *
 */
public class GaussianityFeauture extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int timelag;
	private String name = "Gaussianity";
	private String sname = "GAUSS";
	
	/**
	 * @param t The trajectory for which the gaussianity is to be calulated.
	 * @param timelag Timelag as dimensionless interger (1,2,3...)
	 */
	public GaussianityFeauture(Trajectory t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	@Override
	public double[] evaluate() {
		MeanSquaredDisplacmentFeature msdf = new MeanSquaredDisplacmentFeature(t, timelag);
		QuartricMomentFeature qart = new QuartricMomentFeature(t, timelag);
		
		double msd = msdf.evaluate()[0];
		double q = qart.evaluate()[0];
		
		double res = (2*q)/(3*msd*msd) - 1;
		result = new double[] {res};
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortName() {
		return sname;
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
