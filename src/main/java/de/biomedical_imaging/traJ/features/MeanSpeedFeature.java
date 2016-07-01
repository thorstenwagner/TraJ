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
 * Implements the mean curvilinear speed and the mean straight-line speed according to
 * http://doi.org/10.1016/B978-0-12-391857-4.00009-4
 * 
 * @author thorsten
 *
 */
public class MeanSpeedFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private double timelag;
	
	/**
	 * 
	 * @param t Trajectory where the speed should be calculated for.
	 * @param timelag Timelag between two steps.
	 */
	public MeanSpeedFeature(Trajectory t,double timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	/**
	 * Calculates the mean curvlinear speed and the mean straight-line speed.
	 * @return Double array where the first element is the mean curvilinear speed and the second the mean straight-line speed.
	 */
	public double[] evaluate() {
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i-1).distance(t.get(i))/timelag;
		}
		
		double meanspeed = sum/(t.size()-1);
		
		double netDistance = t.get(0).distance(t.get(t.size()-1));
		double straightLineSpeed = netDistance/((t.size()-1)*timelag);
		result = new double[]{meanspeed,straightLineSpeed};
		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mean Speed Feature";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "MEANSPEED";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
	}

}
