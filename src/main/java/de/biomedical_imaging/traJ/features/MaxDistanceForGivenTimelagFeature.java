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
 * 
 * Calculates the maximum distance between any two points with a given timelag.
 * 
 * @author Thorsten Wagner
 *
 */
public class MaxDistanceForGivenTimelagFeature extends AbstractTrajectoryFeature {
	private Trajectory t;
	private int lag;
	
	/**
	 * @param t Trajectory for which the distance is to be calculated
	 * @param lag Allowed timelag between two positions.
	 */
	public MaxDistanceForGivenTimelagFeature(Trajectory t, int lag) {
		this.t = t;
		this.lag = lag;
	}
	
	/**
	 * @return An double array with the elements [0] = max distance 
	 */
	@Override
	public double[] evaluate() {
		double distance = Double.MIN_NORMAL;
		for(int i = lag; i < t.size(); i++){
			double d = t.get(i-lag).distance(t.get(i)); 
			if(d> distance){
				distance = d;
			}
		}
		result = new double[]{distance};
		return result;
	}

	@Override
	public String getName() {
		return "Max distance for a given timelag";
	}

	@Override
	public String getShortName() {
		return "MAX-DIST-LAG";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
	}

}
