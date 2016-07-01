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
 * Relates the net dispalcement to the sum of step lengths
 */
public class StraightnessFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	
	public StraightnessFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		result = new double[]{getStraightness()};
		return result;
	}
	
	public double getStraightness(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i).distance(t.get(i-1));
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double straightness = (t.get(0).distance(t.get(t.size()-1)))/sum;
		return straightness;
	}

	@Override
	public String getName() {
		return "Straightness";
	}

	@Override
	public String getShortName() {
		
		return "STRAIGHTNESS";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
